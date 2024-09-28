package co.com.inventory.auth.manager;

import co.com.inventory.auth.JwtTokenManagerService;
import co.com.inventory.commons.technicalexception.TechnicalException;
import co.com.inventory.commons.technicalexception.messages.TechnicalExceptionMessage;
import co.com.inventory.model.commons.GeneralStatus;
import co.com.inventory.model.role.Role;
import co.com.inventory.model.user.User;
import co.com.inventory.r2dbc.auth.RoleAdapter;
import co.com.inventory.r2dbc.auth.TokenAdapter;
import co.com.inventory.r2dbc.auth.UserAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple4;
import reactor.util.function.Tuples;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static co.com.inventory.commons.technicalexception.messages.TechnicalExceptionMessage.USER_NOT_FOUND;

@Component
@RequiredArgsConstructor
class JwtAuthenticationManager implements ReactiveAuthenticationManager {

    private static final String ROLE_PREFIX = "ROLE_";
    private final JwtTokenManagerService jwtTokenGeneratorService;
    private final UserAdapter userAdapter;
    private final RoleAdapter roleAdapter;
    private final TokenAdapter tokenAdapter;

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        String token = authentication.getCredentials().toString();
        String username = extractUsername(token);

        return getUserAndRoles(username, token)
                .flatMap(this::validateTokenAndBuildAuthentication)
                .onErrorResume(e -> {
                    if (e instanceof TechnicalException) {
                        return Mono.error(e);
                    }

                    return Mono.error(new TechnicalException(TechnicalExceptionMessage.SECURITY_VALIDATION_FAILED));
                });
    }

    private String extractUsername(String token) {
        try {
            return jwtTokenGeneratorService.extractUsername(token);
        } catch (Exception e) {
            throw new TechnicalException(TechnicalExceptionMessage.INVALID_TOKEN_STRUCTURE);
        }
    }

    private Mono<Tuple4<User, List<Role>, Boolean, String>> getUserAndRoles(String username, String token) {
        Mono<Boolean> isTokenValidMono = tokenAdapter.findByAccessToken(token)
                .filter(tokenFromDatabase -> !tokenFromDatabase.isExpired() && !tokenFromDatabase.isRevoked())
                .hasElement();
        Function<Long, Mono<List<Role>>> rolesMono = userId -> roleAdapter.findByUserId(userId).collectList();

        return userAdapter.findByUsername(username, GeneralStatus.ACTIVE)
                .switchIfEmpty(Mono.defer(() -> Mono.error(new TechnicalException(USER_NOT_FOUND))))
                .flatMap(userRepository -> Mono.zip(rolesMono.apply(userRepository.getId()), isTokenValidMono)
                        .map(tuple2 -> Tuples.of(userRepository, tuple2.getT1(), tuple2.getT2(), token)));
    }

    private Mono<Authentication> validateTokenAndBuildAuthentication(Tuple4<User, List<Role>, Boolean, String> tuple) {
        User user = tuple.getT1();
        List<Role> roles = tuple.getT2();
        boolean isTokenValid = tuple.getT3();
        String token = tuple.getT4();

        if (!isTokenValid && !jwtTokenGeneratorService.isTokenValid(user, token)) {
            return Mono.error(new TechnicalException(TechnicalExceptionMessage.INVALID_TOKEN));
        }

        List<GrantedAuthority> authorities = roles.stream()
                .map(role -> ROLE_PREFIX + role.getName())
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        UserDetails userDetails = new org.springframework.security.core.userdetails.User(user.getUsername(),
                user.getPassword(), authorities);

        return Mono.just(new UsernamePasswordAuthenticationToken(userDetails, null, authorities));
    }
}
