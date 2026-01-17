package com.churncheck.api.infra.security;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;

@Service
public class TokenService {

    @Value("${spring.security.token.secret}")
    private String secret;

	//https://github.com/auth0/java-jwt
    public String createToken(AuthUser user) {
        try {
            var algorithm = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withIssuer("ChurnCheckAPI")
                    .withExpiresAt(expirationDate())
                    .withSubject(user.getEmail())
                    .sign(algorithm);
        } catch (JWTCreationException exception){
            throw new RuntimeException("Error while creating token JWT", exception);
        }
    }

    public String getSubject(String tokenJWT) {
        try {
        	Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer("ChurnCheckAPI")
                    .build()
                    .verify(tokenJWT)
                    .getSubject();
        } catch (JWTVerificationException exception){
            throw new RuntimeException("Invalid JWT");
        }
    }
    
    private Instant expirationDate() {
		return Instant.now().plus(2, ChronoUnit.HOURS);
    }
}