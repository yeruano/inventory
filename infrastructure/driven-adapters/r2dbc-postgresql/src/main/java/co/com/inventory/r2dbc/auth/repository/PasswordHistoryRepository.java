package co.com.inventory.r2dbc.auth.repository;

import co.com.inventory.r2dbc.auth.data.PasswordHistoryData;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface PasswordHistoryRepository extends ReactiveCrudRepository<PasswordHistoryData, Long> {

    Flux<PasswordHistoryData> findByUserId(Long userId);
}
