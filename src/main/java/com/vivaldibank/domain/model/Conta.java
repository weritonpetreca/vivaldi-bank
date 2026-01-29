package com.vivaldibank.domain.model;

import com.vivaldibank.domain.model.exceptions.SaldoInsuficienteException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Conta {

    private UUID id;
    private String numero;
    private BigDecimal saldo;
    private String titularNome;
    private Cpf cpf;
    private LocalDateTime criadoEm;
    private List<Movimentacao> movimentacoes = new ArrayList<>();
    private String senha;
    private String role;

    // Construtor privado para forçar o uso do Builder ou Factory Method
    private Conta(Builder builder) {
        this.id = builder.id;
        this.numero = builder.numero;
        this.titularNome = builder.titularNome;
        this.cpf = builder.cpf;
        this.saldo = builder.saldo;
        this.criadoEm = builder.criadoEm;
        this.movimentacoes = builder.movimentacoes != null ? builder.movimentacoes : new ArrayList<>();
        this.senha = builder.senha;
        this.role = builder.role;
    }

    // Constructor para criar uma NOVA conta (sem ID ainda, saldo zero)
    public Conta(String numero, String titularNome, Cpf cpf, String senha) {
        this.id = UUID.randomUUID();
        this.numero = numero;
        this.titularNome = titularNome;
        this.cpf = cpf;
        this.senha = senha;
        this.role = "USER";
        this.saldo = BigDecimal.ZERO;
        this.criadoEm = LocalDateTime.now();
    }

    // Static Factory Method para iniciar o Builder
    public static Builder builder() {
        return new Builder();
    }

    // Classe Builder interna
    public static class Builder {
        private UUID id;
        private String numero;
        private BigDecimal saldo;
        private String titularNome;
        private Cpf cpf;
        private LocalDateTime criadoEm;
        private List<Movimentacao> movimentacoes;
        private String senha;
        private String role;

        public Builder id(UUID id) { this.id = id; return this; }
        public Builder numero(String numero) { this.numero = numero; return this; }
        public Builder saldo(BigDecimal saldo) { this.saldo = saldo; return this; }
        public Builder titularNome(String titularNome) { this.titularNome = titularNome; return this; }
        public Builder cpf(Cpf cpf) { this.cpf = cpf; return this; }
        public Builder criadoEm(LocalDateTime criadoEm) { this.criadoEm = criadoEm; return this; }
        public Builder movimentacoes(List<Movimentacao> movimentacoes) { this.movimentacoes = movimentacoes; return this; }
        public Builder senha(String senha) { this.senha = senha; return this; }
        public Builder role(String role) { this.role = role; return this; }

        public Conta build() {
            return new Conta(this);
        }
    }

    // Comportamentos - Regras de Negócio

    public void depositar(BigDecimal valor) {
        validarValorPositivo(valor);

        this.saldo = this.saldo.add(valor);

        this.movimentacoes.add(new Movimentacao(TipoMovimentacao.DEPOSITO, valor));
    }

    public void sacar(BigDecimal valor) {
        validarValorPositivo(valor);
        validarSaldoSuficiente(valor);

        this.saldo = this.saldo.subtract(valor);

        this.movimentacoes.add(new Movimentacao(TipoMovimentacao.SAQUE, valor));
    }

    public void debitarTransferenciaEnviada(BigDecimal valor, Conta destino) {
        validarValorPositivo(valor);
        validarSaldoSuficiente(valor);

        this.saldo = this.saldo.subtract(valor);

        this.movimentacoes.add(new Movimentacao(
                TipoMovimentacao.TRANSFERENCIA_ENVIADA,
                valor,
                destino.getTitularNome(),
                destino.getNumero()
        ));
    }

    public void creditarTransferenciaRecebida(BigDecimal valor, Conta origem) {
        validarValorPositivo(valor);

        this.saldo = this.saldo.add(valor);

        this.movimentacoes.add(new Movimentacao(
                TipoMovimentacao.TRANSFERENCIA_RECEBIDA,
                valor,
                origem.getTitularNome(),
                origem.getNumero()
        ));
    }

    private static void validarValorPositivo(BigDecimal valor) {
        if (valor == null || valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Valor deve ser maior que zero");
        }
    }

    private void validarSaldoSuficiente(BigDecimal valor) {
        if (this.saldo.compareTo(valor) < 0) {
            throw new SaldoInsuficienteException("Saldo insuficiente");
        }
    }

    // Getters (Apenas leitura! Sem Setters para proteger o estado)

    public UUID getId() { return id; }
    public String getNumero() { return numero; }
    public String getTitularNome() { return titularNome; }
    public Cpf getCpf() { return cpf; }
    public BigDecimal getSaldo() { return saldo; }
    public LocalDateTime getCriadoEm() { return criadoEm; }
    public List<Movimentacao> getMovimentacoes() { return movimentacoes; }
    public String getSenha() { return senha; }
    public String getRole() { return role; }
}
