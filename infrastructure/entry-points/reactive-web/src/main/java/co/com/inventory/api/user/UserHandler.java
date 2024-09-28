package co.com.inventory.api.user;

import co.com.inventory.api.user.dto.RegisterUserDTO;
import co.com.inventory.api.user.mapper.UserMapper;
import co.com.inventory.api.util.RequestValidator;
import co.com.inventory.commons.utils.RoleConstant;
import co.com.inventory.usecase.user.UserRegistrationUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class UserHandler {

    private final UserRegistrationUseCase userRegistrationUseCase;
    private final RequestValidator requestValidator;

    @Transactional
    @PreAuthorize("hasRole(" + RoleConstant.ROLE_ADMIN + ")")
    public Mono<ServerResponse> registerUser(ServerRequest serverRequest) {
        return requestValidator.validateRequestBody(serverRequest, RegisterUserDTO.class)
                .flatMap(registerUserDTO -> requestValidator.validateBody(registerUserDTO)
                        .map(UserMapper::buildRegisterUser)
                        .flatMap(userRegistrationUseCase::registerUser)
                        .flatMap(ServerResponse.ok()::bodyValue));
    }
}
