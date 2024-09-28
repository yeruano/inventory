package co.com.inventory.r2dbc.auth;

import co.com.inventory.model.user.PasswordHistory;
import co.com.inventory.model.user.gateways.PasswordHistoryGateway;
import co.com.inventory.r2dbc.auth.mapper.PasswordHistoryMapper;
import co.com.inventory.r2dbc.auth.repository.PasswordHistoryRepository;
import co.com.inventory.r2dbc.commons.errorhandler.DatabaseErrorHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
@Transactional
@RequiredArgsConstructor
public class PasswordHistoryAdapter implements PasswordHistoryGateway {

    private final PasswordHistoryRepository passwordHistoryRepository;

    @Override
    public Mono<PasswordHistory> save(PasswordHistory passwordHistory) {
        return passwordHistoryRepository.save(PasswordHistoryMapper.buildPasswordHistoryData(passwordHistory))
                .map(PasswordHistoryMapper::buildPasswordHistory)
                .onErrorMap(DatabaseErrorHandler::handleError);
    }

    @Override
    public Flux<PasswordHistory> findAllByUserId(Long userId) {
        return passwordHistoryRepository.findByUserId(userId)
                .map(PasswordHistoryMapper::buildPasswordHistory)
                .onErrorMap(DatabaseErrorHandler::handleError);
    }
}
