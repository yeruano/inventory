package co.com.inventory.model.auth.gateways;

import co.com.inventory.model.user.User;

import java.util.Map;

public interface TokenManagerGateway {

    String generateToken(User user, Map<String, Object> extraClaims);
    String generateRefreshToken(User user);
    String extractUsername(String token);
    boolean isTokenValid(User user, String token);
}
