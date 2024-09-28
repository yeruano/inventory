package co.com.inventory.model.user.gateways;

import co.com.inventory.model.user.Person;
import reactor.core.publisher.Mono;

public interface PersonGateway {

    Mono<Person> findById(Long personId);
    Mono<Person> save(Person person);
}
