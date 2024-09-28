package co.com.inventory.r2dbc.auth.mapper;

import co.com.inventory.model.user.Person;
import co.com.inventory.model.user.User;
import co.com.inventory.model.commons.GeneralStatus;
import co.com.inventory.r2dbc.auth.data.UserData;
import lombok.experimental.UtilityClass;

@UtilityClass
public class UserMapper {

    public UserData buildUserData(User user, Person person) {
        return UserData.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .status(GeneralStatus.ACTIVE.name())
                .personId(person.getId())
                .build();
    }

    public UserData buildUserData(User user) {
        return UserData.builder()
                .id(user.getId())
                .username(user.getUsername())
                .password(user.getPassword())
                .creationDate(user.getCreationDate())
                .lastAccessDate(user.getLastAccessDate())
                .status(user.getStatus())
                .passwordExpiryDate(user.getPasswordExpiryDate())
                .isPasswordExpired(user.getIsPasswordExpired())
                .build();
    }

    public User buildUser(UserData userData) {
        return User.builder()
                .id(userData.getId())
                .username(userData.getUsername())
                .password(userData.getPassword())
                .creationDate(userData.getCreationDate())
                .lastAccessDate(userData.getLastAccessDate())
                .status(userData.getStatus())
                .passwordExpiryDate(userData.getPasswordExpiryDate())
                .isPasswordExpired(userData.getIsPasswordExpired())
                .build();
    }
}
