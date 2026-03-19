package com.vivaldibank.application.usecases;

import com.vivaldibank.domain.model.Conta;
import com.vivaldibank.domain.model.Cpf;
import com.vivaldibank.domain.model.exceptions.ContaNaoEncontradaException;
import com.vivaldibank.domain.model.exceptions.SaldoInsuficienteException;
import com.vivaldibank.domain.ports.out.ContaRepositoryPort;
import com.vivaldibank.domain.ports.out.NotificacaoPort;
import com.vivaldibank.domain.ports.out.TransactionalPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransacoesUseCaseTest {

    @Mock
    private ContaRepositoryPort contaRepositoryPort;

    @Mock
    private NotificacaoPort notificacaoPort;

    /*
     * Mock do TransactionalPort — ponto mais importante deste arquivo.
     *
     * O TransactionalPort recebe um Runnable e o executa dentro de uma transação.
     * Em testes unitários não há banco nem Spring, então precisamos que o mock
     * simplesmente execute o Runnable imediatamente — sem abrir transação real.
     *
     * doAnswer(inv -> { inv.getArgument(0, Runnable.class).run(); return null; })
     *
     * Traduzindo: "quando execute() for chamado, pegue o primeiro argumento
     * (o Runnable), chame .run() nele e retorne null".
     *
     * Sem isso, o lambda dentro do UseCase nunca seria executado e o teste
     * passaria em branco — o que seria um falso positivo perigoso.
     */
    @Mock
    private TransactionalPort transactionalPort;

    @BeforeEach
    void configurarTransactionalPort() {
        lenient().doAnswer(invocation -> {
            invocation.getArgument(0, Runnable.class).run();
            return null;
        }).when(transactionalPort).execute(any());
    }

    // ── Depósito ─────────────────────────────────────────────────────────────

    @Nested
    @DisplayName("RealizarDepositoUseCase")
    class DepositoTests {

        private RealizarDepositoUseCase useCase;

        @BeforeEach
        void setUp() {
            useCase = new RealizarDepositoUseCase(
                contaRepositoryPort, notificacaoPort, transactionalPort);
        }

        @Test
        @DisplayName("Deve realizar depósito e retornar conta atualizada")
        void deveRealizarDeposito() {
            Conta conta = new Conta("123", "Geralt", new Cpf("09331162685"), "senha");
            when(contaRepositoryPort.buscarPorIdParaAlteracao("1")).thenReturn(Optional.of(conta));
            when(contaRepositoryPort.salvar(any())).thenAnswer(inv -> inv.getArgument(0));

            Conta resultado = useCase.executar("1", new BigDecimal("100.00"));

            assertEquals(0, new BigDecimal("100.00").compareTo(resultado.getSaldo()));
            verify(notificacaoPort).notificar(any(), any());
            verify(contaRepositoryPort).salvar(conta);
        }

        @Test
        @DisplayName("Deve lançar exceção quando conta não for encontrada")
        void deveLancarExcecaoQuandoContaNaoEncontrada() {
            when(contaRepositoryPort.buscarPorIdParaAlteracao("99")).thenReturn(Optional.empty());

            assertThrows(ContaNaoEncontradaException.class,
                () -> useCase.executar("99", new BigDecimal("100.00")));

            verify(contaRepositoryPort, never()).salvar(any());
            verify(notificacaoPort, never()).notificar(any(), any());
        }
    }

    // ── Saque ─────────────────────────────────────────────────────────────────

    @Nested
    @DisplayName("RealizarSaqueUseCase")
    class SaqueTests {

        private RealizarSaqueUseCase useCase;

        @BeforeEach
        void setUp() {
            useCase = new RealizarSaqueUseCase(
                contaRepositoryPort, notificacaoPort, transactionalPort);
        }

        @Test
        @DisplayName("Deve realizar saque quando saldo for suficiente")
        void deveRealizarSaque() {
            Conta conta = new Conta("123", "Geralt", new Cpf("09331162685"), "senha");
            conta.depositar(new BigDecimal("200.00"));

            when(contaRepositoryPort.buscarPorIdParaAlteracao("1")).thenReturn(Optional.of(conta));
            when(contaRepositoryPort.salvar(any())).thenAnswer(inv -> inv.getArgument(0));

            Conta resultado = useCase.executar("1", new BigDecimal("80.00"));

            assertEquals(0, new BigDecimal("120.00").compareTo(resultado.getSaldo()));
            verify(notificacaoPort).notificar(any(), any());
        }

        @Test
        @DisplayName("Deve lançar exceção quando saldo for insuficiente")
        void deveLancarExcecaoQuandoSaldoInsuficiente() {
            Conta conta = new Conta("123", "Geralt", new Cpf("09331162685"), "senha");
            conta.depositar(new BigDecimal("50.00"));

            when(contaRepositoryPort.buscarPorIdParaAlteracao("1")).thenReturn(Optional.of(conta));

            assertThrows(SaldoInsuficienteException.class,
                () -> useCase.executar("1", new BigDecimal("100.00")));

            verify(contaRepositoryPort, never()).salvar(any());
        }

        @Test
        @DisplayName("Deve lançar exceção quando conta não for encontrada")
        void deveLancarExcecaoQuandoContaNaoEncontrada() {
            when(contaRepositoryPort.buscarPorIdParaAlteracao("99")).thenReturn(Optional.empty());

            assertThrows(ContaNaoEncontradaException.class,
                () -> useCase.executar("99", new BigDecimal("50.00")));
        }
    }

    // ── Transferência ─────────────────────────────────────────────────────────

    @Nested
    @DisplayName("RealizarTransferenciaUseCase")
    class TransferenciaTests {

        private RealizarTransferenciaUseCase useCase;

        @BeforeEach
        void setUp() {
            useCase = new RealizarTransferenciaUseCase(
                contaRepositoryPort, notificacaoPort, transactionalPort);
        }

        @Test
        @DisplayName("Deve transferir valor entre duas contas distintas")
        void deveRealizarTransferencia() {
            Conta origem = new Conta("123", "Geralt", new Cpf("09331162685"), "senha");
            Conta destino = new Conta("456", "Yennefer", new Cpf("18409972069"), "senha");
            origem.depositar(new BigDecimal("300.00"));

            when(contaRepositoryPort.buscarPorIdParaAlteracao("1")).thenReturn(Optional.of(origem));
            when(contaRepositoryPort.buscarPorIdParaAlteracao("2")).thenReturn(Optional.of(destino));
            when(contaRepositoryPort.salvar(any())).thenAnswer(inv -> inv.getArgument(0));

            useCase.executar("1", "2", new BigDecimal("100.00"));

            assertEquals(0, new BigDecimal("200.00").compareTo(origem.getSaldo()));
            assertEquals(0, new BigDecimal("100.00").compareTo(destino.getSaldo()));
            verify(contaRepositoryPort, times(2)).salvar(any());
            verify(notificacaoPort, times(2)).notificar(any(), any());
        }

        @Test
        @DisplayName("Deve lançar exceção quando origem e destino forem a mesma conta")
        void deveLancarExcecaoQuandoMesmaConta() {
            assertThrows(IllegalArgumentException.class,
                () -> useCase.executar("1", "1", new BigDecimal("100.00")));

            // transação nem deve ser aberta para validação de negócio simples
            verify(transactionalPort, never()).execute(any());
        }

        @Test
        @DisplayName("Deve lançar exceção quando saldo da origem for insuficiente")
        void deveLancarExcecaoQuandoSaldoInsuficiente() {
            Conta origem = new Conta("123", "Geralt", new Cpf("09331162685"), "senha");
            Conta destino = new Conta("456", "Yennefer", new Cpf("18409972069"), "senha");
            origem.depositar(new BigDecimal("50.00"));

            when(contaRepositoryPort.buscarPorIdParaAlteracao("1")).thenReturn(Optional.of(origem));
            when(contaRepositoryPort.buscarPorIdParaAlteracao("2")).thenReturn(Optional.of(destino));

            assertThrows(SaldoInsuficienteException.class,
                () -> useCase.executar("1", "2", new BigDecimal("100.00")));

            verify(contaRepositoryPort, never()).salvar(any());
        }
    }
}
