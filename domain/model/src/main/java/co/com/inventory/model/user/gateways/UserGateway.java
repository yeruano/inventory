package co.com.inventory.model.user.gateways;

import co.com.inventory.model.user.Person;
import co.com.inventory.model.user.User;
import co.com.inventory.model.commons.GeneralStatus;
import reactor.core.publisher.Mono;

public interface UserGateway {

    Mono<User> findActiveUserByPersonId(Long personId);
    Mono<User> findByUsername(String username, GeneralStatus status);
    Mono<User> save(User user, Person person);
    Mono<User> changePassword(User user);
}
