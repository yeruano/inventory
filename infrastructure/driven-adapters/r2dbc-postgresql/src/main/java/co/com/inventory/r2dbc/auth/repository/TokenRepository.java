package co.com.inventory.r2dbc.auth.repository;

import co.com.inventory.r2dbc.auth.data.TokenData;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface TokenRepository extends ReactiveCrudRepository<TokenData, Long> {

    @Query("SELECT * FROM token INNER JOIN \"user\" on \"user\".id = token.user_id\n " +
           "WHERE \"user\".id = :userId and (token.expired = false and token.revoked = false)")
    Flux<TokenData> findAllValidTokenByUser(Long userId);

    Mono<TokenData> findByAccessToken(String accessToken);
}
