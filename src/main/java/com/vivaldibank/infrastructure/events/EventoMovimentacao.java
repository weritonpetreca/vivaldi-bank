package com.vivaldibank.infrastructure.events;

import java.math.BigDecimal;

public record EventoMovimentacao(
    String idMovimentacao,
    String dataHora,
    String contaOrigem,
    BigDecimal valor,
    String tipo,
    String contaContraparte
) {}
