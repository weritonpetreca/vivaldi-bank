package com.vivaldibank.infrastructure.adapters.in.web.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record TransferenciaRequest(

        @NotNull(message = "O ID da conta de destino é obrigatório")
        String idDestino,

        @NotNull(message = "O valor da transferência é obrigatório")
        @Positive(message = "O valor da transferência deve ser maior que zero")
        BigDecimal valor
) {}
