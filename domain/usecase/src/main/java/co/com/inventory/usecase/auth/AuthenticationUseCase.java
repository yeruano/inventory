package co.com.inventory.usecase.auth;

import co.com.inventory.model.auth.AuthorizationToken;
import co.com.inventory.model.auth.LoginUser;
import co.com.inventory.model.auth.Token;
import co.com.inventory.model.auth.gateways.PasswordEncoderGateway;
import co.com.inventory.model.auth.gateways.TokenGateway;
import co.com.inventory.model.auth.gateways.TokenManagerGateway;
import co.com.inventory.model.auth.gateways.UserAuthenticationGateway;
import co.com.inventory.model.commons.GeneralStatus;
import co.com.inventory.model.commons.TokenType;
import co.com.inventory.model.exception.BusinessException;
import co.com.inventory.model.role.Role;
import co.com.inventory.model.role.gateways.RoleGateway;
import co.com.inventory.model.user.User;
import co.com.inventory.model.user.gateways.UserGateway;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuple3;
import reactor.util.function.Tuples;

import java.util.List;
import java.util.Map;

import static co.com.inventory.model.exception.messages.BusinessExceptionMessage.INVALID_CREDENTIALS;
import static co.com.inventory.model.exception.messages.BusinessExceptionMessage.PASSWORD_CHANGE_REQUIRED;
import static co.com.inventory.model.exception.messages.BusinessExceptionMessage.USERNAME_NOT_FOUND;

@RequiredArgsConstructor
public class AuthenticationUseCase {

    private static final String ROLES = "roles";
    private static final boolean TOKEN_NOT_REVOKED = false;
    private static final boolean TOKEN_REVOKED = true;
    private static final boolean TOKEN_NOT_EXPIRED = false;
    private static final boolean TOKEN_EXPIRED = true;
    private final UserGateway userGateway;
    private final RoleGateway roleGateway;
    private final PasswordEncoderGateway passwordEncoderGateway;
    private final TokenManagerGateway tokenManagerGateway;
    private final TokenGateway tokenGateway;
    private final UserAuthenticationGateway userAuthenticationGateway;

    public Mono<AuthorizationToken> login(LoginUser loginUser) {
        return userGateway.findByUsername(loginUser.getUsername(), GeneralStatus.ACTIVE)
                .switchIfEmpty(Mono.error(new BusinessException(INVALID_CREDENTIALS)))
                .filter(user -> !user.getIsPasswordExpired())
                .switchIfEmpty(Mono.error(new BusinessException(PASSWORD_CHANGE_REQUIRED)))
                .flatMap(user -> this.revokeAllUserTokens(user).thenReturn(user))
                .filter(user -> passwordEncoderGateway.matches(loginUser.getPassword(), user.getPassword()))
                .switchIfEmpty(Mono.defer(() -> Mono.error(new BusinessException(INVALID_CREDENTIALS))))
                .flatMap(this::findRolesByUserId)
                .flatMap(this::saveToken)
                .flatMap(tuple3 -> userAuthenticationGateway.authenticate(tuple3.getT1(), tuple3.getT2())
                        .thenReturn(tuple3.getT3()));
    }

    public Mono<AuthorizationToken> refreshToken(String refreshToken) {
        return Mono.just(tokenManagerGateway.extractUsername(refreshToken))
                .flatMap(username -> userGateway.findByUsername(username, GeneralStatus.ACTIVE))
                .switchIfEmpty(Mono.defer(() -> Mono.error(new BusinessException(USERNAME_NOT_FOUND))))
                .filter(user -> tokenManagerGateway.isTokenValid(user, refreshToken))
                .flatMap(user -> this.revokeAllUserTokens(user).thenReturn(user))
                .flatMap(this::findRolesByUserId)
                .flatMap(this::saveToken)
                .flatMap(tuple3 -> userAuthenticationGateway.authenticate(tuple3.getT1(), tuple3.getT2())
                        .thenReturn(tuple3.getT3()));
    }

    public Mono<Void> logout(String accessToken) {
        return tokenGateway.findByAccessToken(accessToken)
                .flatMap(token -> {
                    token.setRevoked(TOKEN_REVOKED);
                    token.setExpired(TOKEN_EXPIRED);
                    return tokenGateway.save(token);
                })
                .then(userAuthenticationGateway.logout());
    }

    private Mono<Tuple2<User, List<Role>>> findRolesByUserId(User user) {
        return roleGateway.findByUserId(user.getId())
                .collectList()
                .map(roles -> Tuples.of(user, roles));
    }

    private Mono<Tuple3<User, List<Role>, AuthorizationToken>> saveToken(Tuple2<User, List<Role>> tuple2) {
        AuthorizationToken authorizationToken = buildAuthorizationToken(tuple2.getT1(), tuple2.getT2());
        Token token = Token.builder()
               .accessToken(authorizationToken.getAccessToken())
               .refreshToken(authorizationToken.getRefreshToken())
               .type(TokenType.BEARER.name())
               .revoked(TOKEN_NOT_REVOKED)
               .expired(TOKEN_NOT_EXPIRED)
               .userId(tuple2.getT1().getId())
               .build();

        return tokenGateway.save(token)
                .thenReturn(Tuples.of(tuple2.getT1(), tuple2.getT2(), authorizationToken));
    }

    private AuthorizationToken buildAuthorizationToken(User user, List<Role> roles) {
        List<String> rolesAsList = roles.stream().map(Role::getName).toList();
        return AuthorizationToken.builder()
                .accessToken(tokenManagerGateway.generateToken(user, Map.of(ROLES, rolesAsList)))
                .refreshToken(tokenManagerGateway.generateRefreshToken(user))
                .build();
    }

    private Mono<Void> revokeAllUserTokens(User user) {
        return tokenGateway.findAllValidTokenByUser(user.getId())
                .map(token -> token.toBuilder().revoked(TOKEN_REVOKED).expired(TOKEN_EXPIRED).build())
                .collectList()
                .flatMap(tokens -> tokenGateway.saveAll(tokens).then());
    }
}
