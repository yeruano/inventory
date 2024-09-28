package co.com.inventory.model.auth.gateways;

import co.com.inventory.model.auth.Token;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface TokenGateway {

    Mono<Token> save(Token token);
    Flux<Token> saveAll(List<Token> tokens);
    Flux<Token> findAllValidTokenByUser(Long userId);
    Mono<Token> findByAccessToken(String accessToken);
}
