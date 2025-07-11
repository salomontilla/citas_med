package com.salomon.citasmedbackend.domain.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.salomon.citasmedbackend.domain.usuario.DetallesUsuario;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Component
public class JwtUtil {
    @Value("${api.security.secret}")
    private String SECRET_KEY;

    public String generateToken(DetallesUsuario user) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
            return JWT.create()
                    .withIssuer("Salomontilla")
                    .withSubject(user.getUsername())
                    .withClaim("username", user.getUsername())
                    .withExpiresAt(generateExpirationTime())
                    .sign(algorithm);
        } catch (JWTCreationException exception){
            throw new RuntimeException(exception);
        }
    }

    public Instant generateExpirationTime() {
        return Instant.now().plus(1, ChronoUnit.HOURS);
    }

    public boolean validateToken(String token) {
        Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
        JWTVerifier verifier = JWT.require(algorithm)
                .withIssuer("Salomontilla")
                .build();

        verifier.verify(token);
        return true;
    }

    public String extractUsername(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
            DecodedJWT decodedJWT = JWT.require(algorithm)
                    .withIssuer("Salomontilla")
                    .build()
                    .verify(token);

            return decodedJWT.getSubject();

        } catch (TokenExpiredException e) {
            throw new TokenExpiredException(e.getMessage(), e.getExpiredOn());
        }

    }
}

