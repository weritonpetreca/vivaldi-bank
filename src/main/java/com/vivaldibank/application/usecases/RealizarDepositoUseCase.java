package com.vivaldibank.application.usecases;

import com.vivaldibank.domain.model.Conta;
import com.vivaldibank.domain.model.Movimentacao;
import com.vivaldibank.domain.model.exception.ContaNaoEncontradaException;
import com.vivaldibank.domain.ports.out.ContaRepositoryPort;
import com.vivaldibank.domain.ports.out.NotificacaoPort;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

public class RealizarDepositoUseCase {

    private final ContaRepositoryPort contaRepositoryPort;
    private final NotificacaoPort notificacaoPort;

    public RealizarDepositoUseCase(ContaRepositoryPort contaRepositoryPort,
                                   NotificacaoPort notificacaoPort) {
        this.contaRepositoryPort = contaRepositoryPort;
        this.notificacaoPort = notificacaoPort;
    }

    @Transactional
    public Conta executar(String id, BigDecimal valor) {

        Conta conta = contaRepositoryPort.buscarPorIdParaAlteracao(id)
                .orElseThrow(() -> new ContaNaoEncontradaException(id));

        conta.depositar(valor);

        Conta contaSalva = contaRepositoryPort.salvar(conta);

        Movimentacao ultimaMovimentacao = conta.getMovimentacoes().getLast();
        notificacaoPort.notificar(ultimaMovimentacao, conta.getNumero());

        return contaSalva;
    }


}
