package com.vivaldibank.domain.ports.in;

import com.vivaldibank.domain.model.CpfValidator;

import java.math.BigDecimal;

/**
 * Command: Representa a intenção de realizar uma operação
 * Carrega todos os dados necessários para o UseCase trabalhar
 */

public record CriarContaCommand(
        String titularNome,
        String cpf,
        BigDecimal depositoInicial
) {
    
    public CriarContaCommand {
        if (titularNome == null || titularNome.isBlank()) {
            throw new IllegalArgumentException("Nome do titular é obrigatório");
        }
        if (cpf == null || cpf.isBlank()) {
            throw new IllegalArgumentException("CPF do titular é obrigatório");
        }
        if (!CpfValidator.isValid(cpf)) {
            throw new IllegalArgumentException("CPF inválido");
        }
        if (depositoInicial == null || depositoInicial.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Depósito inicial deve ser maior que zero");
        }
        
    }

}
