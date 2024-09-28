package co.com.inventory.api.auth;

import co.com.inventory.api.auth.dto.LoginUserDTO;
import co.com.inventory.api.auth.mapper.AuthenticationMapper;
import co.com.inventory.api.commons.DataDTO;
import co.com.inventory.api.util.RequestValidator;
import co.com.inventory.usecase.auth.AuthenticationUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class AuthenticationHandler {

    private static final String BEARER = "Bearer ";
    private final AuthenticationUseCase authenticationUseCase;
    private final RequestValidator requestValidator;

    public Mono<ServerResponse> login(ServerRequest serverRequest) {
        return requestValidator.validateRequestBody(serverRequest, LoginUserDTO.class)
                .flatMap(loginUserDTO -> requestValidator.validateBody(loginUserDTO)
                        .map(AuthenticationMapper::buildLoginUser)
                        .flatMap(authenticationUseCase::login)
                        .map(AuthenticationMapper::buildTokenDTO)
                        .map(DataDTO::buildDataDTO)
                        .flatMap(ServerResponse.ok()::bodyValue));
    }

    @Transactional
    public Mono<ServerResponse> refreshToken(ServerRequest serverRequest) {
        String authHeader = serverRequest.headers().firstHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith(BEARER)) {
            return ServerResponse.badRequest().bodyValue("Invalid or missing token");
        }

        String refreshToken = authHeader.substring(BEARER.length());
        return authenticationUseCase.refreshToken(refreshToken)
                .map(AuthenticationMapper::buildTokenDTO)
                .map(DataDTO::buildDataDTO)
                .flatMap(ServerResponse.ok()::bodyValue);
    }

    public Mono<ServerResponse> logout(ServerRequest serverRequest) {
        String authHeader = serverRequest.headers().firstHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith(BEARER)) {
            return ServerResponse.badRequest().bodyValue("Invalid or missing token");
        }

        String accessToken = authHeader.substring(BEARER.length());
        return authenticationUseCase.logout(accessToken)
                .then(ServerResponse.ok().bodyValue("Logout successfully"));

    }
}
