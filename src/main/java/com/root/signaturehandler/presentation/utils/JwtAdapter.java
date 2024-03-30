package com.root.signaturehandler.presentation.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.root.signaturehandler.presentation.exceptions.BadRequestException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

@Component
public class JwtAdapter {
    @Value("${api.security.token.secret}")
    protected String privateKey;
    protected String issuer = "pdf-handler-api";
    protected Integer expirationTime = 2;
    
    public String generate(UUID subject) {
        Algorithm algorithm = Algorithm.HMAC256(this.privateKey);

        try {
            return JWT.create().withIssuer(this.issuer).withSubject(subject.toString())
                    .withExpiresAt(this.tokenExpiration())
                    .sign(algorithm);
        } catch (JWTCreationException exception) {
            throw new BadRequestException(exception.getMessage());
        }
    }

    public String verify(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(this.privateKey);

            JWTVerifier verifier = JWT.require(algorithm).withIssuer(this.issuer).build();

            return verifier.verify(token).getSubject();
        } catch (JWTDecodeException exception) {
            throw new BadRequestException(exception.getMessage());
        }
    }

    private Instant tokenExpiration() {
        return LocalDateTime.now().plusHours(this.expirationTime).atOffset(ZoneOffset.of("+0")).toInstant();
    }
}
