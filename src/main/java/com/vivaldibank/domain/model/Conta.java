package com.vivaldibank.domain.model;

import com.vivaldibank.domain.model.exception.SaldoInsuficienteException;

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
    private String cpf;
    private LocalDateTime criadoEm;
    private List<Movimentacao> movimentacoes = new ArrayList<>();
    private String senha;
    private String role;

    // Constructor para criar uma NOVA conta (sem ID ainda, saldo zero)
    public Conta(String numero, String titularNome, String cpf, String senha) {
        this.id = UUID.randomUUID();
        this.numero = numero;
        this.titularNome = titularNome;

        String cpfLimpo = (cpf != null) ? cpf.replaceAll("\\D","") : null;
        if (cpfLimpo == null || !CpfValidator.isValid(cpfLimpo)) {
            throw new IllegalArgumentException("CPF inválido");
        }

        this.cpf = cpfLimpo;
        this.senha = senha;
        this.role = "USER";
        this.saldo = BigDecimal.ZERO;
        this.criadoEm = LocalDateTime.now();
        this.movimentacoes = new ArrayList<>();
    }

    // Constructor para RECONSTRUIR uma conta que veio do banco de dados (Adapter vai usar)
    public Conta(UUID id, String numero, String titularNome, String cpf, BigDecimal saldo, LocalDateTime criadoEm, List<Movimentacao> movimentacoes, String senha, String role) {
        this.id = id;
        this.numero = numero;
        this.titularNome = titularNome;
        this.cpf = (cpf != null) ? cpf.replaceAll("\\D","") : null;
        this.saldo = saldo;
        this.criadoEm = criadoEm;
        this.movimentacoes = movimentacoes != null ? movimentacoes : new ArrayList<>();
        this.senha = senha;
        this.role = role;
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
    public String getCpf() { return cpf; }
    public BigDecimal getSaldo() { return saldo; }
    public LocalDateTime getCriadoEm() { return criadoEm; }
    public List<Movimentacao> getMovimentacoes() { return movimentacoes; }
    public String getSenha() { return senha; };
    public String getRole() { return role; };
}
