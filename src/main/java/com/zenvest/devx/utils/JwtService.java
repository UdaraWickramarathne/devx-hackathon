package com.zenvest.devx.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JwtService is responsible for handling JWT operations.
 * It provides methods to extract claims, build tokens, and validate tokens.
 */
@Service
public class JwtService {
    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.access-token.expiry-ms}")
    private long accessTokenExpiry;

    /**
     * Retrieves the signing key for JWT operations.
     * This method uses the secret key defined in the application properties.
     *
     * @return a SecretKey object used for signing and verifying JWT tokens
     */
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Extracts the username from the provided JWT token.
     * This method uses the extractClaim method to retrieve the subject claim.
     *
     * @param token the JWT token to extract the username from
     * @return the extracted username
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extracts a specific claim from the provided JWT token.
     * This method uses a resolver function to extract the desired claim.
     *
     * @param token   the JWT token to extract claims from
     * @param resolver a function that resolves the desired claim from the Claims object
     * @param <T>    the type of the claim to be extracted
     * @return the extracted claim of type T
     */
    public <T> T extractClaim(String token, java.util.function.Function<Claims, T> resolver) {
        Claims claims = extractAllClaims(token);
        return resolver.apply(claims);
    }

    /**
     * Extracts all claims from the provided JWT token.
     * This method uses the signing key to parse the token and retrieve its claims.
     *
     * @param token the JWT token to extract claims from
     * @return a Claims object containing all claims in the token
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .setAllowedClockSkewSeconds(2)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Builds a JWT token with the specified username and claims.
     * The token is signed using the HS256 algorithm and includes an expiration time.
     *
     * @param username the username to include in the token
     * @return the generated JWT token
     */
    public String buildToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + accessTokenExpiry))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Validates the provided JWT token.
     * This method checks if the token is valid by parsing it with the signing key.
     *
     * @param token the JWT token to validate
     * @return true if the token is valid, false otherwise
     */
    public boolean isTokenValid(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
