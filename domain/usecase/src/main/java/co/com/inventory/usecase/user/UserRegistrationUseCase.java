package co.com.inventory.usecase.user;

import co.com.inventory.model.auth.gateways.PasswordEncoderGateway;
import co.com.inventory.model.commons.GeneralStatus;
import co.com.inventory.model.exception.BusinessException;
import co.com.inventory.model.exception.user.UsernameAlreadyExistException;
import co.com.inventory.model.role.Role;
import co.com.inventory.model.role.gateways.RoleGateway;
import co.com.inventory.model.user.PasswordHistory;
import co.com.inventory.model.user.Person;
import co.com.inventory.model.user.RegisterUser;
import co.com.inventory.model.user.User;
import co.com.inventory.model.user.UserRoleRelationship;
import co.com.inventory.model.user.gateways.PasswordHistoryGateway;
import co.com.inventory.model.user.gateways.PersonGateway;
import co.com.inventory.model.user.gateways.UserGateway;
import co.com.inventory.model.user.gateways.UserRoleRelationshipGateway;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.time.LocalDateTime;
import java.util.List;

import static co.com.inventory.model.exception.messages.BusinessExceptionMessage.EMPTY_ROLE_LIST;

@RequiredArgsConstructor
public class UserRegistrationUseCase {

    private final PersonGateway personGateway;
    private final RoleGateway roleGateway;
    private final UserGateway userGateway;
    private final PasswordEncoderGateway passwordEncoderGateway;
    private final PasswordHistoryGateway passwordHistoryGateway;
    private final UserRoleRelationshipGateway userRoleRelationshipGateway;

    public Mono<User> registerUser(RegisterUser registerUser) {
        Mono<Person> personMono = personGateway.save(registerUser.getPerson());
        Mono<List<Role>> rolListMono = roleGateway.findByIds(getRoleIds(registerUser.getRoles())).collectList();

        return Mono.zip(personMono, rolListMono)
                .filter(tuple2 -> !tuple2.getT2().isEmpty())
                .switchIfEmpty(Mono.defer(() -> Mono.error(new BusinessException(EMPTY_ROLE_LIST))))
                .flatMap(tuple2 -> saveUser(registerUser, tuple2))
                .flatMap(this::saveUserRoleRelationship)
                .map(Tuple2::getT1);
    }

    private List<Long> getRoleIds(List<Role> roles) {
        return roles.stream().map(Role::getId).toList();
    }

    private Mono<Tuple2<User, List<Role>>> saveUser(RegisterUser registerUser, Tuple2<Person, List<Role>> tuple2) {
        return checkUserExists(registerUser.getUser().getUsername())
                .filter(userExists -> !userExists)
                .switchIfEmpty(Mono.defer(() -> Mono.error(new UsernameAlreadyExistException())))
                .flatMap(userExists -> encodeAndSaveUser(registerUser, tuple2));
    }

    private Mono<Boolean> checkUserExists(String username) {
        return userGateway.findByUsername(username, GeneralStatus.ACTIVE)
                .hasElement();
    }

    private Mono<Tuple2<User, List<Role>>> encodeAndSaveUser(RegisterUser registerUser,
                                                             Tuple2<Person, List<Role>> tuple2) {
        var encodePassword = passwordEncoderGateway.encode(registerUser.getUser().getPassword());
        registerUser.getUser().setPassword(encodePassword);
        return userGateway.save(registerUser.getUser(), tuple2.getT1())
                .flatMap(user -> passwordHistoryGateway.save(buildPasswordHistory(user)).thenReturn(user))
                .map(user -> Tuples.of(user, tuple2.getT2()));
    }

    private Mono<Tuple2<User, List<Role>>> saveUserRoleRelationship(Tuple2<User, List<Role>> tuple2) {
        List<UserRoleRelationship> userRoleRelationships = tuple2.getT2().stream()
                .map(role -> UserRoleRelationship.builder()
                        .userId(tuple2.getT1().getId())
                        .roleId(role.getId())
                        .build())
                .toList();

        return userRoleRelationshipGateway.saveAll(userRoleRelationships)
                .collectList()
                .thenReturn(tuple2);
    }

    private PasswordHistory buildPasswordHistory(User user) {
        return PasswordHistory.builder()
                .password(user.getPassword())
                .changeDate(LocalDateTime.now())
                .userId(user.getId())
                .build();
    }
}
