package com.vivaldibank.infrastructure.adapters.in.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CriarContaRequest(

        @NotBlank(message = "O nome do titular é obrigatório")
        String nomeTitular,

        @NotBlank(message = "O CPF é obrigatório")
        String cpf,

        @NotNull
        BigDecimal depositoInicial,

        @NotBlank
        String senha
) {}
