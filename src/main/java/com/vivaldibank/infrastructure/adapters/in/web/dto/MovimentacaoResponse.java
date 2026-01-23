package com.vivaldibank.infrastructure.adapters.in.web.dto;

import java.math.BigDecimal;

public record MovimentacaoResponse(
        String id,
        String dataHora,
        BigDecimal valor,
        String tipo,
        String contraparteNome,
        String contraparteNumeroConta
) {}
