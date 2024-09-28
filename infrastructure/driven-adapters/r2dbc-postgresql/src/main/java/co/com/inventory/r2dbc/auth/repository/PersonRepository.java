package co.com.inventory.r2dbc.auth.repository;

import co.com.inventory.r2dbc.auth.data.PersonData;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends ReactiveCrudRepository<PersonData, Long> {
}
