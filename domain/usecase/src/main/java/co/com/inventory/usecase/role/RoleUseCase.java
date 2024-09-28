package co.com.inventory.usecase.role;

import co.com.inventory.model.role.Role;
import co.com.inventory.model.role.gateways.RoleGateway;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.List;

@RequiredArgsConstructor
public class RoleUseCase {

    private final RoleGateway roleGateway;

    public Mono<Role> findRoleById(Long roleId) {
        return roleGateway.findById(roleId);
    }

    public Mono<List<Role>> findAllRoles() {
        return roleGateway.findAll().collectList();
    }

    public Mono<Role> saveRole(Role role) {
        return roleGateway.save(role);
    }

    public Mono<Role> updateRoleById(Long roleId, Role role) {
        return roleGateway.updateById(roleId, role);
    }

    public Mono<Void> deleteRoleById(Long roleId) {
        return roleGateway.deleteById(roleId);
    }
}
