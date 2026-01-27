package com.vivaldibank.infrastructure.events;

public record EventoLogin(
    String accountId,
    String usuarioMascarado,
    String tipo
) {
}
