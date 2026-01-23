package com.vivaldibank.infrastructure.adapters.in.web.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record ContaResponse(
        String id,
        String numero,
        String titularNome,
        String cpf,
        BigDecimal saldo,
        LocalDateTime criadoEm,
        List<MovimentacaoResponse> movimentacoes
) {}
