package co.com.inventory.r2dbc.auth;

import co.com.inventory.commons.technicalexception.TechnicalException;
import co.com.inventory.model.role.Role;
import co.com.inventory.model.role.gateways.RoleGateway;
import co.com.inventory.r2dbc.auth.data.UserRoleData;
import co.com.inventory.r2dbc.auth.mapper.RoleMapper;
import co.com.inventory.r2dbc.auth.repository.RoleRepository;
import co.com.inventory.r2dbc.auth.repository.UserRoleRepository;
import co.com.inventory.r2dbc.commons.errorhandler.DatabaseErrorHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static co.com.inventory.commons.technicalexception.messages.TechnicalExceptionMessage.RESOURCE_NOT_FOUND;

@Repository
@Transactional
@RequiredArgsConstructor
public class RoleAdapter implements RoleGateway {

    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;

    @Override
    public Mono<Role> findById(Long roleId) {
        return roleRepository.findById(roleId)
                .map(RoleMapper::buildRole)
                .switchIfEmpty(Mono.defer(() -> Mono.error(new TechnicalException(RESOURCE_NOT_FOUND))))
                .onErrorMap(DatabaseErrorHandler::handleError);
    }

    @Override
    public Flux<Role> findAll() {
        return roleRepository.findAll()
                .map(RoleMapper::buildRole)
                .onErrorMap(DatabaseErrorHandler::handleError);
    }

    @Override
    public Flux<Role> findByUserId(Long userId) {
        return userRoleRepository.findByUserId(userId)
                .collectList()
                .flatMapMany(userRoleDataList -> this.findByIds(getIds(userRoleDataList)))
                .onErrorMap(DatabaseErrorHandler::handleError);
    }

    @Override
    public Flux<Role> findByIds(List<Long> ids) {
        return roleRepository.findAllById(ids)
                .map(RoleMapper::buildRole)
                .onErrorMap(DatabaseErrorHandler::handleError);
    }

    @Override
    public Mono<Role> save(Role role) {
        return roleRepository.save(RoleMapper.buildRoleData(role))
                .map(RoleMapper::buildRole)
                .onErrorMap(DatabaseErrorHandler::handleError);
    }

    @Override
    public Mono<Role> updateById(Long roleId, Role role) {
        return this.findById(roleId)
                .flatMap(roleFromDatabase -> this.save(role))
                .onErrorMap(DatabaseErrorHandler::handleError);
    }

    @Override
    public Mono<Void> deleteById(Long roleId) {
        return this.findById(roleId)
                .flatMap(roleFromDatabase -> roleRepository.deleteById(roleId))
                .onErrorMap(DatabaseErrorHandler::handleError);
    }

    private List<Long> getIds(List<UserRoleData> roles) {
        return roles.stream().map(UserRoleData::getRoleId)
                .toList();
    }
}
