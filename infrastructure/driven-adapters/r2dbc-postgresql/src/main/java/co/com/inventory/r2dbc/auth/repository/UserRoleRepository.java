package co.com.inventory.r2dbc.auth.repository;

import co.com.inventory.r2dbc.auth.data.UserRoleData;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface UserRoleRepository extends ReactiveCrudRepository<UserRoleData, Long> {

    Flux<UserRoleData> findAllByUserId(Long userId);
    Flux<UserRoleData> findByUserId(Long userId);
}
