package back_ptitulo.service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class JwtService {

    // Secret key for signing the JWTs (secure and ideally loaded from environment variables)
    private final Key secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256); // HMAC-SHA256

    // Token expiration time (e.g., 24 hours)
    private static final long EXPIRATION_TIME = 24 * 60 * 60 * 1000; // 24 hours in milliseconds

    public String generateToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(secretKey)
                .compact();
    }

    /**
     * Validates the provided JWT token.
     *
     * @param token The JWT token to validate.
     * @return The email (subject) from the token if valid.
     * @throws JwtException if the token is invalid or expired.
     */
    public String validateToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (JwtException e) {
            throw new IllegalArgumentException("Invalid or expired token");
        }
    }
}
