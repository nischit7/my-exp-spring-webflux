package com.nischit.myexp.webflux.netty.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

import lombok.extern.slf4j.Slf4j;

/**
 * Helps in JWT validation.
 */
@Slf4j
@Component
public class JwtHelper {

    private final String jwtSecret;
    private final long jwtExpirationMs;

    @Autowired
    public JwtHelper(
            @Value("${app.jwtSecret:12345678901234567890}") final String jwtSecret,
            @Value("${app.jwtExpirationMs:14400000}") final long jwtExpirationMs) {
        this.jwtSecret = jwtSecret;
        this.jwtExpirationMs = jwtExpirationMs;
    }

    /**
     * Extracts username from the token.
     *
     * @param token JWT token.
     * @return Username.
     */
    public String getUserNameFromJwtToken(final String token) {
        return Jwts.parser().setSigningKey(this.jwtSecret).parseClaimsJws(token).getBody().getSubject();
    }

    /**
     * Validates JWT token.
     *
     * @param authToken An auth token.
     * @return Returns {@code true}, if valid.
     */
    public boolean validateJwtToken(final String authToken) {
        try {
            Jwts.parser().setSigningKey(this.jwtSecret).parseClaimsJws(authToken);
        } catch (final SignatureException | MalformedJwtException | ExpiredJwtException | UnsupportedJwtException ex) {
            throw new BadCredentialsException("Jwt token is invalid", ex);
        }
        return true;
    }
}
