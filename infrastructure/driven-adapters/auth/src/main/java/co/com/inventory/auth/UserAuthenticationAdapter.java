package co.com.inventory.auth;

import co.com.inventory.commons.technicalexception.TechnicalException;
import co.com.inventory.commons.technicalexception.messages.TechnicalExceptionMessage;
import co.com.inventory.model.auth.AuthenticatedUser;
import co.com.inventory.model.role.Role;
import co.com.inventory.model.user.User;
import co.com.inventory.model.auth.gateways.UserAuthenticationGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserAuthenticationAdapter implements UserAuthenticationGateway {

    private static final String ROLE_PREFIX = "ROLE_";
    private final JwtTokenManagerService jwtTokenGeneratorService;

    @Override
    public Mono<Void> authenticate(User user, List<Role> roles) {
        List<GrantedAuthority> authorities = roles.stream()
                .map(role -> ROLE_PREFIX + role.getName())
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        UserDetails userDetails = new org.springframework.security.core.userdetails.User(user.getUsername(),
                user.getPassword(), authorities);
        var authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        return ReactiveSecurityContextHolder.getContext()
                .map(securityContext -> {
                    securityContext.setAuthentication(authenticationToken);
                    return securityContext;
                })
                .then();
    }

    @Override
    public Mono<Void> logout() {
        return Mono.deferContextual(contextView -> {
//            Context context = Context.of(contextView.stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));
//            Context updatedContext = ReactiveSecurityContextHolder.clearContext().apply(context);
//            return Mono.defer(() -> Mono.empty().then()).contextWrite(updatedContext);
            Context updatedContext = ReactiveSecurityContextHolder.clearContext().apply(Context.of(contextView));
            return Mono.defer(() -> Mono.empty().then()).contextWrite(updatedContext);
        });
    }

    @Override
    public Mono<AuthenticatedUser> getAuthenticatedUser() {
        return ReactiveSecurityContextHolder.getContext()
                .flatMap(securityContext -> {
                    Authentication authentication = securityContext.getAuthentication();
                    if (authentication != null && authentication.getCredentials() != null) {
                        String token = authentication.getCredentials().toString();
                        String username = jwtTokenGeneratorService.extractUsername(token);
                        List<String> roles = jwtTokenGeneratorService.extractRoles(token);

                        return Mono.just(new AuthenticatedUser(username, roles));
                    }

                    return Mono.error(new TechnicalException(TechnicalExceptionMessage.USER_NOT_LOGGED_IN));
                });
    }
}
