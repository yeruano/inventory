package co.com.inventory.r2dbc.auth;

import co.com.inventory.model.user.UserRoleRelationship;
import co.com.inventory.model.user.gateways.UserRoleRelationshipGateway;
import co.com.inventory.r2dbc.auth.mapper.UserRoleRelationshipMapper;
import co.com.inventory.r2dbc.auth.repository.UserRoleRepository;
import co.com.inventory.r2dbc.commons.errorhandler.DatabaseErrorHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;

import java.util.List;

@Repository
@Transactional
@RequiredArgsConstructor
public class UserRoleRelationshipAdapter implements UserRoleRelationshipGateway {

    private final UserRoleRepository userRoleRepository;

    @Override
    public Flux<UserRoleRelationship> findAllByUserId(Long userId) {
        return userRoleRepository.findAllByUserId(userId)
                .map(UserRoleRelationshipMapper::buildUserRoleRelationship)
                .onErrorMap(DatabaseErrorHandler::handleError);
    }

    @Override
    public Flux<UserRoleRelationship> saveAll(List<UserRoleRelationship> userRoleRelationships) {
        return userRoleRepository.saveAll(UserRoleRelationshipMapper.buildUserRoleDataList(userRoleRelationships))
                .map(UserRoleRelationshipMapper::buildUserRoleRelationship)
                .onErrorMap(DatabaseErrorHandler::handleError);
    }
}
