package com.vivaldibank.infrastructure.adapters.in.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record CriarContaRequest(

        @NotBlank(message = "O nome do titular é obrigatório")
        String nomeTitular,

        @NotBlank(message = "O CPF é obrigatório")
        String cpf,

        @NotNull
        @PositiveOrZero(message = "O depósito inicial não pode ser negativo")
        BigDecimal depositoInicial,

        @NotBlank
        String senha
) {}
