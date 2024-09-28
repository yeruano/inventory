package co.com.inventory.model.user.gateways;

import co.com.inventory.model.user.PasswordHistory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PasswordHistoryGateway {

    Mono<PasswordHistory> save(PasswordHistory passwordHistory);
    Flux<PasswordHistory> findAllByUserId(Long userId);
}
