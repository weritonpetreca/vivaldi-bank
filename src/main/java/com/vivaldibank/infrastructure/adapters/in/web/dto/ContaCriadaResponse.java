package com.vivaldibank.infrastructure.adapters.in.web.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record ContaCriadaResponse(
    UUID id,
    String numeroConta,
    String titularNome,
    BigDecimal saldo,
    String token
) {
}
