package co.com.inventory.model.user.gateways;

import co.com.inventory.model.user.UserRoleRelationship;
import reactor.core.publisher.Flux;

import java.util.List;

public interface UserRoleRelationshipGateway {

    Flux<UserRoleRelationship> findAllByUserId(Long userId);
    Flux<UserRoleRelationship> saveAll(List<UserRoleRelationship> userRoleRelationships);
}
