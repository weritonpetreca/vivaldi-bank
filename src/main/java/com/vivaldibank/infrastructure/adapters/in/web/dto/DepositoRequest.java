package com.vivaldibank.infrastructure.adapters.in.web.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record DepositoRequest(
        @NotNull(message = "O valor do depósito é obrigatório")
        @Positive(message = "O valor do depósito deve ser maior que zero")
        BigDecimal valor
) {}
