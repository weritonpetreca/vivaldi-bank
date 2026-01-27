package com.vivaldibank.infrastructure.events;

public record EventoContaCriada(
    String idConta,
    String numeroConta,
    String titular,
    String tipo
) {
}
