package com.vivaldibank.infrastructure.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    // Em produção, isso vem de variável de ambiente (application.yml)
    // Por enquanto, deixamos fixo para facilitar
    @Value("{api.security.token.secret:segredo-bruxo}")
    private String secret;

    private static final String ISSUER = "vivaldi-bank-api";

    public String gerarToken(String subject) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.create()
                .withIssuer(ISSUER)         // Quem emitiu
                .withSubject(subject)            // Quem é o dono (CPF)
                .withExpiresAt(gerarDataExpiracao())    // Quando vence
                .sign(algorithm);                       // Assina
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Erro ao gerar token JWT", exception);
        }
    }

    public String validarToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                .withIssuer("vivaldi-bank-api")
                .build()
                .verify(token)                          // Se for inválido, lança exceção
                .getSubject();                          // Retorna o CPF que estava dentro do token
        } catch (JWTVerificationException exception) {
            return "";                                  // Token inválido ou expirado
        }
    }

    private Instant gerarDataExpiracao() {
        // Token vence em 2 horas (Zona -03:00 Brasil)
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}
