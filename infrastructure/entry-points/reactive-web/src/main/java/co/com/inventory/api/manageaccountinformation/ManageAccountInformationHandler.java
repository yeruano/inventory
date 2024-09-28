package co.com.inventory.api.manageaccountinformation;

import co.com.inventory.api.manageaccountinformation.dto.ChangePasswordDTO;
import co.com.inventory.api.manageaccountinformation.mapper.ManageAccountInformationMapper;
import co.com.inventory.api.util.RequestValidator;
import co.com.inventory.usecase.manageaccountinformation.ManageAccountInformationUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class ManageAccountInformationHandler {

    private final ManageAccountInformationUseCase manageAccountInformationUseCase;
    private final RequestValidator requestValidator;

    public Mono<ServerResponse> changePassword(ServerRequest serverRequest) {
        return requestValidator.validateRequestBody(serverRequest, ChangePasswordDTO.class)
                .flatMap(changePasswordDTO -> requestValidator.validateBody(changePasswordDTO)
                        .map(ManageAccountInformationMapper::buildChangePassword)
                        .flatMap(manageAccountInformationUseCase::changePassword)
                        .then(ServerResponse.ok().bodyValue("Password changed successfully")));
    }
}
