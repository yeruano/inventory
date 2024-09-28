package co.com.inventory.r2dbc.auth;

import co.com.inventory.commons.technicalexception.TechnicalException;
import co.com.inventory.model.auth.Token;
import co.com.inventory.model.auth.gateways.TokenGateway;
import co.com.inventory.r2dbc.auth.mapper.TokenMapper;
import co.com.inventory.r2dbc.auth.repository.TokenRepository;
import co.com.inventory.r2dbc.commons.errorhandler.DatabaseErrorHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static co.com.inventory.commons.technicalexception.messages.TechnicalExceptionMessage.TOKEN_NOT_FOUND;

@Repository
@Transactional
@RequiredArgsConstructor
public class TokenAdapter implements TokenGateway {

    private final TokenRepository tokenRepository;

    @Override
    public Mono<Token> save(Token token) {
        return tokenRepository.save(TokenMapper.buildTokenData(token))
                .map(TokenMapper::buildToken)
                .onErrorMap(DatabaseErrorHandler::handleError);
    }

    @Override
    public Flux<Token> saveAll(List<Token> tokens) {
        var tokenDataList = TokenMapper.buildTokenDataList(tokens);
        return tokenRepository.saveAll(tokenDataList)
                .map(TokenMapper::buildToken)
                .onErrorMap(DatabaseErrorHandler::handleError);
    }

    @Override
    public Flux<Token> findAllValidTokenByUser(Long userId) {
        return tokenRepository.findAllValidTokenByUser(userId)
                .map(TokenMapper::buildToken)
                .onErrorMap(DatabaseErrorHandler::handleError);
    }

    @Override
    public Mono<Token> findByAccessToken(String accessToken) {
        return tokenRepository.findByAccessToken(accessToken)
                .switchIfEmpty(Mono.defer(() -> Mono.error(new TechnicalException(TOKEN_NOT_FOUND))))
                .map(TokenMapper::buildToken)
                .onErrorMap(DatabaseErrorHandler::handleError);
    }
}
