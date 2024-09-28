package co.com.inventory.auth.filter;

import co.com.inventory.auth.JwtTokenManagerService;
import co.com.inventory.model.auth.gateways.TokenGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter implements WebFilter {

    private final ReactiveAuthenticationManager reactiveAuthenticationManager;
    private final JwtTokenManagerService jwtTokenGeneratorService;
    private final TokenGateway tokenGateway;
    private static final String BEARER = "Bearer ";

    @Override
    public Mono<Void> filter(@NonNull ServerWebExchange exchange, @NonNull WebFilterChain chain) {
        String path = exchange.getRequest().getPath().toString();
        if (path.startsWith("/auth/")) {
            return chain.filter(exchange);
        }

        String token = extractToken(exchange.getRequest().getHeaders());
        if (token == null) {
            return chain.filter(exchange);
        }
        String username = jwtTokenGeneratorService.extractUsername(token);
        List<String> roles = jwtTokenGeneratorService.extractRoles(token);
        List<GrantedAuthority> authorities = roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        var authenticationToken = new UsernamePasswordAuthenticationToken(username, token, authorities);

        return tokenGateway.findByAccessToken(token)
                .filter(tokenFromDB -> !tokenFromDB.isExpired() && !tokenFromDB.isRevoked())
                .flatMap(tokenFromDB -> reactiveAuthenticationManager.authenticate(authenticationToken)
                        .subscribeOn(Schedulers.boundedElastic())
                        .flatMap(authentication -> chain.filter(exchange)
                                .contextWrite(ReactiveSecurityContextHolder.withAuthentication(authentication))))
                .switchIfEmpty(Mono.defer(() -> chain.filter(exchange)));
    }

    private String extractToken(HttpHeaders headers) {
        String bearerToken = headers.getFirst(HttpHeaders.AUTHORIZATION);
        if (bearerToken != null && bearerToken.startsWith(BEARER)) {
            return bearerToken.substring(BEARER.length());
        }
        return null;
    }
}
