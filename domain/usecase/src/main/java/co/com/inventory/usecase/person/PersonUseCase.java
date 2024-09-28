package co.com.inventory.usecase.person;

import co.com.inventory.model.user.Person;
import co.com.inventory.model.user.RegisterUser;
import co.com.inventory.model.user.User;
import co.com.inventory.model.user.gateways.PersonGateway;
import co.com.inventory.model.auth.gateways.UserAuthenticationGateway;
import co.com.inventory.model.user.gateways.UserGateway;
import co.com.inventory.model.user.gateways.UserRoleRelationshipGateway;
import co.com.inventory.model.role.Role;
import co.com.inventory.model.role.gateways.RoleGateway;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.util.List;

@RequiredArgsConstructor
public class PersonUseCase {

    private static final String ROLE_ADMIN = "ADMIN";
    private final UserAuthenticationGateway userAuthenticationGateway;
    private final PersonGateway personGateway;
    private final UserGateway userGateway;
    private final UserRoleRelationshipGateway userRoleRelationshipGateway;
    private final RoleGateway roleGateway;

    public Mono<RegisterUser> findPerson(Long personId) {
        return personGateway.findById(personId)
                .flatMap(person -> userGateway.findActiveUserByPersonId(person.getId())
                        .map(user -> Tuples.of(person, user)))
                .flatMap(tuple2 -> findRoles(tuple2)
                        .map(roles -> buildRegisterUser(roles, tuple2)));
    }

    public Mono<Person> updatePerson(Person person) {
        return userAuthenticationGateway.getAuthenticatedUser()
                .filter(authenticatedUser -> isAdmin(authenticatedUser.getRoles()))
                .flatMap(authenticatedUser -> updatePersonFull(person))
                .switchIfEmpty(Mono.defer(() -> updatePersonPartial(person)));
    }

    private Mono<List<Role>> findRoles(Tuple2<Person, User> tuple2) {
        return userRoleRelationshipGateway.findAllByUserId(tuple2.getT2().getId())
                .flatMap(userRoleRelationship -> roleGateway.findById(userRoleRelationship.getRoleId()))
                .collectList();
    }

    private RegisterUser buildRegisterUser(List<Role> roles, Tuple2<Person, User> tuple2) {
        return RegisterUser.builder()
                .person(tuple2.getT1())
                .user(tuple2.getT2())
                .roles(roles)
                .build();
    }

    private boolean isAdmin(List<String> roles) {
        return roles.stream().anyMatch(role -> role.equals(ROLE_ADMIN));
    }

    private Mono<Person> updatePersonFull(Person person) {
        return personGateway.save(person);
    }

    private Mono<Person> updatePersonPartial(Person person) {
        return personGateway.findById(person.getId())
                .map(personFromStorage -> buildPeronToUpdate(person, personFromStorage))
                .flatMap(personGateway::save);
    }

    private Person buildPeronToUpdate(Person person, Person personFromStorage) {
        return Person.builder()
                .id(person.getId())
                .names(personFromStorage.getNames())
                .surnames(personFromStorage.getSurnames())
                .address(person.getAddress())
                .cellPhoneNumber(person.getCellPhoneNumber())
                .email(person.getEmail())
                .build();
    }
}
