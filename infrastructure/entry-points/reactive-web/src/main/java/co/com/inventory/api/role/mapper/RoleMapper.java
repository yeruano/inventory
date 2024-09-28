package co.com.inventory.api.role.mapper;

import co.com.inventory.api.role.dto.RoleDTO;
import co.com.inventory.api.role.dto.RoleListDTO;
import co.com.inventory.model.role.Role;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.Objects;

@UtilityClass
public class RoleMapper {

    public RoleListDTO buildRoleListDTO(List<Role> roles) {
        return RoleListDTO.builder()
                .roles(roles.stream().map(RoleMapper::buildRoleDTO).toList())
                .build();
    }

    public RoleDTO buildRoleDTO(Role role) {
        return RoleDTO.builder()
                .id(role.getId())
                .name(role.getName())
                .build();
    }

    public RoleListDTO buildRoleListDTO(Role role) {
        return RoleListDTO.builder()
                .roles(List.of(RoleMapper.buildRoleDTO(role)))
                .build();
    }

    public Role buildRole(RoleDTO roleDTO) {
        var role = Role.builder()
                .name(roleDTO.getName())
                .build();

        return Objects.nonNull(roleDTO.getId()) ? role.toBuilder().id(roleDTO.getId()).build() : role;
    }
}
