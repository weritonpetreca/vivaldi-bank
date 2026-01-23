package com.vivaldibank.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class Movimentacao {

    private UUID id;
    private LocalDateTime dataHora;
    private BigDecimal valor;
    private TipoMovimentacao tipo;
    private String contraparteNome;
    private String contraparteNumeroConta;

    // Construtor Completo (Usado pelo Mapper)
    public Movimentacao(UUID id,
                        LocalDateTime dataHora,
                        BigDecimal valor,
                        TipoMovimentacao tipo,
                        String contraparteNome,
                        String contraparteNumeroConta) {
        this.id = id;
        this.dataHora = dataHora;
        this.valor = valor;
        this.tipo = tipo;
        this.contraparteNome = contraparteNome;
        this.contraparteNumeroConta = contraparteNumeroConta;
    }

    // Construtor Simples (Saque/Depósito)
    public Movimentacao(TipoMovimentacao tipo, BigDecimal valor) {
        this(UUID.randomUUID(), LocalDateTime.now(), valor, tipo, null, null);
    }

    // Construtor Transferência (Com contraparte)
    public Movimentacao(TipoMovimentacao tipo, BigDecimal valor, String contraparteNome, String contraparteNumeroConta) {
        this(UUID.randomUUID(), LocalDateTime.now(), valor, tipo, contraparteNome, contraparteNumeroConta);
    }

    public UUID getId() {
        return id;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public TipoMovimentacao getTipo() {
        return tipo;
    }

    public String getContraparteNome() { return contraparteNome; }

    public String getContraparteNumeroConta() { return contraparteNumeroConta; }
}
