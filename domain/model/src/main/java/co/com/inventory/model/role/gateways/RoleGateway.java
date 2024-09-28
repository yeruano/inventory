package co.com.inventory.model.role.gateways;

import co.com.inventory.model.role.Role;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface RoleGateway {

    Mono<Role> findById(Long roleId);
    Flux<Role> findAll();
    Flux<Role> findByUserId(Long userId);
    Flux<Role> findByIds(List<Long> ids);
    Mono<Role> save(Role role);
    Mono<Role> updateById(Long roleId, Role role);
    Mono<Void> deleteById(Long roleId);
}
