package co.com.inventory.api.user.mapper;

import co.com.inventory.api.role.dto.RoleDTO;
import co.com.inventory.api.role.mapper.RoleMapper;
import co.com.inventory.api.user.dto.PersonDTO;
import co.com.inventory.api.user.dto.RegisterUserDTO;
import co.com.inventory.api.user.dto.UserDTO;
import co.com.inventory.model.role.Role;
import co.com.inventory.model.user.Person;
import co.com.inventory.model.user.RegisterUser;
import co.com.inventory.model.user.User;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.Objects;

@UtilityClass
public class UserMapper {

    public RegisterUser buildRegisterUser(RegisterUserDTO registerUserDTO) {
        return RegisterUser.builder()
                .person(buildPerson(registerUserDTO.getPerson()))
                .user(buildUser(registerUserDTO.getUser()))
                .roles(buildRoles(registerUserDTO.getRoles()))
                .build();
    }

    public Person buildPerson(PersonDTO personDTO) {
        var person = Person.builder()
                .names(personDTO.getNames())
                .surnames(personDTO.getSurnames())
                .address(personDTO.getAddress())
                .cellPhoneNumber(personDTO.getCellPhoneNumber())
                .email(personDTO.getEmail())
                .build();

        return Objects.nonNull(personDTO.getId()) ? person.toBuilder().id(personDTO.getId()).build() : person;
    }

    public User buildUser(UserDTO userDTO) {
        var user = User.builder()
                .username(userDTO.getUsername())
                .password(userDTO.getPassword())
                .build();

        if (Objects.nonNull(userDTO.getId())) {
            user.setId(userDTO.getId());
        }

        if (Objects.nonNull(userDTO.getCreationDate())) {
            user.setCreationDate(userDTO.getCreationDate());
        }

        if (Objects.nonNull(userDTO.getLastAccessDate())) {
            user.setLastAccessDate(userDTO.getLastAccessDate());
        }

        if (Objects.nonNull(userDTO.getStatus())) {
            user.setStatus(userDTO.getStatus());
        }

        return user;
    }

    public List<Role> buildRoles(List<RoleDTO> roleDTOList) {
        return roleDTOList.stream().map(RoleMapper::buildRole).toList();
    }
}
