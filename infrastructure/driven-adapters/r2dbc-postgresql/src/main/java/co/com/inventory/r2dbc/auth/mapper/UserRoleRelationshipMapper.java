package co.com.inventory.r2dbc.auth.mapper;

import co.com.inventory.model.user.UserRoleRelationship;
import co.com.inventory.r2dbc.auth.data.UserRoleData;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class UserRoleRelationshipMapper {

    public List<UserRoleData> buildUserRoleDataList(List<UserRoleRelationship> userRoleRelationships) {
        return userRoleRelationships.stream().map(UserRoleRelationshipMapper::buildUserRoleData).toList();
    }

    public UserRoleData buildUserRoleData(UserRoleRelationship userRoleRelationship) {
        return UserRoleData.builder()
                .userId(userRoleRelationship.getUserId())
                .roleId(userRoleRelationship.getRoleId())
                .build();
    }

    public UserRoleRelationship buildUserRoleRelationship(UserRoleData userRoleData) {
        return UserRoleRelationship.builder()
                .userId(userRoleData.getUserId())
                .roleId(userRoleData.getRoleId())
                .build();
    }
}
