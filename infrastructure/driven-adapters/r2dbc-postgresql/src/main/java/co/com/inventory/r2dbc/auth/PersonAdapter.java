package co.com.inventory.r2dbc.auth;

import co.com.inventory.model.user.Person;
import co.com.inventory.model.user.gateways.PersonGateway;
import co.com.inventory.r2dbc.auth.mapper.PersonMapper;
import co.com.inventory.r2dbc.commons.errorhandler.DatabaseErrorHandler;
import co.com.inventory.r2dbc.auth.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Repository
@Transactional
@RequiredArgsConstructor
public class PersonAdapter implements PersonGateway {

    private final PersonRepository personRepository;

    @Override
    public Mono<Person> findById(Long personId) {
        return personRepository.findById(personId)
                .map(PersonMapper::buildPerson)
                .onErrorMap(DatabaseErrorHandler::handleError);
    }

    @Override
    public Mono<Person> save(Person person) {
        return personRepository.save(PersonMapper.buildPersonData(person))
                .map(PersonMapper::buildPerson)
                .onErrorMap(DatabaseErrorHandler::handleError);
    }
}
