package co.com.inventory.r2dbc.auth.mapper;

import co.com.inventory.model.role.Role;
import co.com.inventory.r2dbc.auth.data.RoleData;
import lombok.experimental.UtilityClass;

import java.util.Objects;

@UtilityClass
public class RoleMapper {

    public RoleData buildRoleData(Role role) {
        var roleData = RoleData.builder().name(role.getName()).build();

        return Objects.nonNull(role.getId()) ? roleData.toBuilder().id(role.getId()).build() : roleData;
    }

    public Role buildRole(RoleData roleData) {
        return Role.builder().id(roleData.getId()).name(roleData.getName()).build();
    }
}
