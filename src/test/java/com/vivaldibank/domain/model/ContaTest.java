package com.vivaldibank.domain.model;

import com.vivaldibank.domain.model.exception.SaldoInsuficienteException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ContaTest {

    @Test
    @DisplayName("Deve depositar valor positivo corretamente")
    void deveDepositarValor() {
        Conta conta = new Conta("123", "Geralt", "093.311.626-85", "senha");

        conta.depositar(new BigDecimal("100.00"));

        assertEquals(new BigDecimal("100.00"), conta.getSaldo());
        assertEquals(1, conta.getMovimentacoes().size());
        assertEquals(TipoMovimentacao.DEPOSITO, conta.getMovimentacoes().getFirst().getTipo());
    }

    @Test
    @DisplayName("Deve sacar valor quando saldo for suficiente")
    void deveSacarComSucesso() {
        Conta conta = new Conta("123", "Geralt", "093.311.626-85", "senha");

        conta.depositar(new BigDecimal("100.00"));
        conta.sacar(new BigDecimal("40.00"));

        assertEquals(0, new BigDecimal("60.00").compareTo(conta.getSaldo()));
        assertEquals(2, conta.getMovimentacoes().size());
    }

    @Test
    @DisplayName("Não deve sacar valor maior que o saldo")
    void naoDeveSacarSemSaldo() {
        Conta conta = new Conta("123", "Geralt", "093.311.626-85", "senha");

        conta.depositar(new BigDecimal("50.00"));

        BigDecimal valorSaque = new BigDecimal("51.00");

        Executable acao = () -> conta.sacar(valorSaque);

        assertThrows(SaldoInsuficienteException.class, acao);
    }
}
