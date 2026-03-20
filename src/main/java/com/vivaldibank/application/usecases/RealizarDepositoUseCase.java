package com.vivaldibank.application.usecases;

import com.vivaldibank.domain.model.Conta;
import com.vivaldibank.domain.model.Movimentacao;
import com.vivaldibank.domain.model.exceptions.ContaNaoEncontradaException;
import com.vivaldibank.domain.ports.out.ContaRepositoryPort;
import com.vivaldibank.domain.ports.out.NotificacaoPort;
import com.vivaldibank.domain.ports.out.TransactionalPort;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicReference;

public class RealizarDepositoUseCase {

    private final ContaRepositoryPort contaRepositoryPort;
    private final NotificacaoPort notificacaoPort;
    private final TransactionalPort transactionalPort;

    public RealizarDepositoUseCase(ContaRepositoryPort contaRepositoryPort,
                                   NotificacaoPort notificacaoPort,
                                   TransactionalPort transactionalPort) {
        this.contaRepositoryPort = contaRepositoryPort;
        this.notificacaoPort = notificacaoPort;
        this.transactionalPort = transactionalPort;
    }

    public Conta executar(String id, BigDecimal valor) {
        AtomicReference<Conta> contaSalvaRef = new AtomicReference<>();

        transactionalPort.execute(() -> {
            Conta conta = contaRepositoryPort.buscarPorIdParaAlteracao(id)
                .orElseThrow(() -> new ContaNaoEncontradaException(id));

            conta.depositar(valor);

            Conta contaSalva = contaRepositoryPort.salvar(conta);
            contaSalvaRef.set(contaSalva);

            Movimentacao ultimaMovimentacao = conta.getMovimentacoes().getLast();
            notificacaoPort.notificar(ultimaMovimentacao, conta.getNumero());
        });

        return contaSalvaRef.get();
    }


}
