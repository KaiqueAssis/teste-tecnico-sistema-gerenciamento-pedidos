package org.teste.casetecnico.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.teste.casetecnico.model.Users;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    @Value("${api.security.token.secret}")
    private String secret;
    public String generateToken(Users user) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret.getBytes());
            return JWT.create()
                    .withIssuer("auth0")
                    .withSubject(user.getUsername())
                    .withExpiresAt(genExpirationDate())
                    .sign(algorithm);
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Erro durante a criação do token", exception);
        }

    }

    private Instant genExpirationDate(){
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }

    public String validateToken(String token){
        try{
            Algorithm algorithm = Algorithm.HMAC256(secret.getBytes());
            return JWT.require(algorithm)
                    .withIssuer("auth0")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException exception){
        return "";
        }
    }
}
