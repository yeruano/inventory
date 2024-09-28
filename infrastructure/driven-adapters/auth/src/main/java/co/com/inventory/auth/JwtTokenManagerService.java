package co.com.inventory.auth;

import co.com.inventory.commons.technicalexception.TechnicalException;
import co.com.inventory.commons.technicalexception.messages.TechnicalExceptionMessage;
import co.com.inventory.model.user.User;
import co.com.inventory.model.auth.gateways.TokenManagerGateway;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtTokenManagerService implements TokenManagerGateway {

    private final String secretKey;
    private final long jwtExpiration;
    private final long refreshExpiration;

    public JwtTokenManagerService(@Value("${security.jwt.secret-key}") String secretKey,
                                  @Value("${security.jwt.expiration}") long jwtExpiration,
                                  @Value("${security.jwt.refresh-token.expiration}") long refreshExpiration) {
        this.secretKey = secretKey;
        this.jwtExpiration = jwtExpiration;
        this.refreshExpiration = refreshExpiration;
    }

    @Override
    public String generateToken(User user, Map<String, Object> extraClaims) {
        return buildToken(extraClaims, user, jwtExpiration);
    }

    @Override
    public String generateRefreshToken(User user) {
        return buildToken(new HashMap<>(), user, refreshExpiration);
    }

    @Override
    public boolean isTokenValid(User user, String token) {
        try {
            final String username = extractUsername(token);
            return username.equals(user.getUsername()) && !isTokenExpired(token);
        } catch (Exception e){
            return false;
        }
    }

    @Override
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private String buildToken(Map<String, Object> extraClaims, User user, long expiration) {
        ZoneId zoneId = ZoneId.of("America/Bogota");
        ZonedDateTime now = ZonedDateTime.now(zoneId);
        ZonedDateTime expirationTime = now.plus(Duration.ofMillis(expiration));

        return Jwts.builder()
                .claims(extraClaims)
                .subject(user.getUsername())
                .issuedAt(Date.from(now.toInstant()))
                .expiration(Date.from(expirationTime.toInstant()))
                .signWith(getSignInKey())
                .compact();
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    @SuppressWarnings("unchecked")
    public List<String> extractRoles(String token) {
        return extractClaim(token, claims -> claims.get("roles", List.class));
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(getSignInKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (SignatureException | ExpiredJwtException | UnsupportedJwtException e) {
            throw new TechnicalException(e, TechnicalExceptionMessage.INVALID_TOKEN);
        } catch (MalformedJwtException | IllegalArgumentException e) {
            throw new TechnicalException(e, TechnicalExceptionMessage.INVALID_TOKEN_STRUCTURE);
        }
    }

    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
