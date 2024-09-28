package co.com.inventory.r2dbc.auth.mapper;

import co.com.inventory.model.auth.Token;
import co.com.inventory.r2dbc.auth.data.TokenData;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.Objects;

@UtilityClass
public class TokenMapper {

    public TokenData buildTokenData(Token token) {
        return TokenData.builder()
                .id(Objects.nonNull(token.getId()) ? token.getId() : null)
                .accessToken(token.getAccessToken())
                .refreshToken(token.getRefreshToken())
                .type(token.getType())
                .expired(token.isExpired())
                .revoked(token.isRevoked())
                .userId(token.getUserId())
                .build();
    }

    public Token buildToken(TokenData tokenData) {
        return Token.builder()
                .id(tokenData.getId())
                .accessToken(tokenData.getAccessToken())
                .refreshToken(tokenData.getRefreshToken())
                .type(tokenData.getType())
                .expired(tokenData.isExpired())
                .revoked(tokenData.isRevoked())
                .userId(tokenData.getUserId())
                .build();
    }

    public List<TokenData> buildTokenDataList(List<Token> tokens) {
        return tokens.stream()
                .map(TokenMapper::buildTokenData)
                .toList();
    }
}
