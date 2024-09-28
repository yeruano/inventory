package co.com.inventory.api.auth;

import co.com.inventory.api.config.ApplicationRoute;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
@RequiredArgsConstructor
public class AuthenticationRouter {

    private final ApplicationRoute applicationRoute;

    @Bean
    public RouterFunction<ServerResponse> authRoutes(AuthenticationHandler authenticationHandler) {
        final String LOGIN_PATH = applicationRoute.getAuthentication() + "/login";
        final String REFRESH_TOKEN_PATH = applicationRoute.getAuthentication() + "/refresh-token";
        final String LOGOUT_PATH = applicationRoute.getAuthentication() + "/logout";
        return RouterFunctions.route().nest(RequestPredicates.accept(MediaType.APPLICATION_JSON), builder -> builder
                        .POST(LOGIN_PATH, authenticationHandler::login)
                        .POST(REFRESH_TOKEN_PATH, authenticationHandler::refreshToken)
                        .POST(LOGOUT_PATH, authenticationHandler::logout))
                .build();
    }
}
