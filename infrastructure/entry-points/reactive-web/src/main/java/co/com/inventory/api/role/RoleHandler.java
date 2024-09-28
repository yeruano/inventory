package co.com.inventory.api.role;

import co.com.inventory.api.commons.DataDTO;
import co.com.inventory.api.config.ApplicationRoute;
import co.com.inventory.api.role.dto.RoleDTO;
import co.com.inventory.api.role.mapper.RoleMapper;
import co.com.inventory.api.util.RequestValidator;
import co.com.inventory.commons.utils.PathVariableConstant;
import co.com.inventory.commons.utils.RoleConstant;
import co.com.inventory.usecase.role.RoleUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;

@Component
@RequiredArgsConstructor
public class RoleHandler {

    private final RoleUseCase roleUseCase;
    private final RequestValidator requestValidator;
    private final ApplicationRoute applicationRoute;

    @PreAuthorize("hasRole(" + RoleConstant.ROLE_ADMIN + ")")
    public Mono<ServerResponse> findRoleById(ServerRequest serverRequest) {
        long roleId = Long.parseLong(serverRequest.pathVariable(PathVariableConstant.ROLE_ID));
        return roleUseCase.findRoleById(roleId)
                .map(RoleMapper::buildRoleListDTO)
                .map(DataDTO::buildDataDTO)
                .flatMap(ServerResponse.ok()::bodyValue);
    }

    @PreAuthorize("hasRole(" + RoleConstant.ROLE_ADMIN + ")")
    public Mono<ServerResponse> findAllRoles(ServerRequest serverRequest) {
        return roleUseCase.findAllRoles()
                .map(RoleMapper::buildRoleListDTO)
                .map(DataDTO::buildDataDTO)
                .flatMap(ServerResponse.ok()::bodyValue);
    }

    @PreAuthorize("hasRole(" + RoleConstant.ROLE_ADMIN + ")")
    public Mono<ServerResponse> saveRole(ServerRequest serverRequest) {
        return requestValidator.validateRequestBody(serverRequest, RoleDTO.class)
                .flatMap(roleDTO -> requestValidator.validateBody(roleDTO)
                        .map(RoleMapper::buildRole)
                        .flatMap(roleUseCase::saveRole)
                        .map(RoleMapper::buildRoleDTO)
                        .map(DataDTO::buildDataDTO)
                        .flatMap(roleDTODataDTO -> ServerResponse.created(createURI(roleDTODataDTO.getData().getId()))
                                .bodyValue(roleDTODataDTO)));
    }

    @PreAuthorize("hasRole(" + RoleConstant.ROLE_ADMIN + ")")
    public Mono<ServerResponse> updateRoleById(ServerRequest serverRequest) {
        long roleId = Long.parseLong(serverRequest.pathVariable(PathVariableConstant.ROLE_ID));
        return requestValidator.validateRequestBody(serverRequest, RoleDTO.class)
                .flatMap(roleDTO -> requestValidator.validateBody(roleDTO)
                        .map(RoleMapper::buildRole)
                        .flatMap(role -> roleUseCase.updateRoleById(roleId, role))
                        .map(RoleMapper::buildRoleDTO)
                        .map(DataDTO::buildDataDTO)
                        .flatMap(ServerResponse.ok()::bodyValue));
    }

    @PreAuthorize("hasRole(" + RoleConstant.ROLE_ADMIN + ")")
    public Mono<ServerResponse> deleteRoleById(ServerRequest serverRequest) {
        long roleId = Long.parseLong(serverRequest.pathVariable(PathVariableConstant.ROLE_ID));
        return roleUseCase.deleteRoleById(roleId).then(ServerResponse.noContent().build());
    }

    private URI createURI(Long path) {
        return URI.create(applicationRoute.getRoles() + "/" + path);
    }
}
