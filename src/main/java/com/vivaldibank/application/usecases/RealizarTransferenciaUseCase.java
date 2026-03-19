package com.vivaldibank.application.usecases;

import com.vivaldibank.domain.model.Conta;
import com.vivaldibank.domain.model.Movimentacao;
import com.vivaldibank.domain.model.exceptions.ContaNaoEncontradaException;
import com.vivaldibank.domain.ports.out.ContaRepositoryPort;
import com.vivaldibank.domain.ports.out.NotificacaoPort;
import com.vivaldibank.domain.ports.out.TransactionalPort;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

public class RealizarTransferenciaUseCase {

    private final ContaRepositoryPort contaRepositoryPort;
    private final NotificacaoPort notificacaoPort;
    private final TransactionalPort transactionalPort;

    public RealizarTransferenciaUseCase(ContaRepositoryPort contaRepositoryPort,
                                        NotificacaoPort notificacaoPort,
                                        TransactionalPort transactionalPort) {
        this.contaRepositoryPort = contaRepositoryPort;
        this.notificacaoPort = notificacaoPort;
        this.transactionalPort = transactionalPort;
    }

    public void executar(String idOrigem, String idDestino, BigDecimal valor) {

        if (idOrigem.equals(idDestino)) {
            throw new IllegalArgumentException("Não é possível transferir para a mesma conta");
        }

        transactionalPort.execute(() -> {
            Conta origem = contaRepositoryPort.buscarPorIdParaAlteracao(idOrigem)
                .orElseThrow(() -> new ContaNaoEncontradaException(idOrigem));

            Conta destino = contaRepositoryPort.buscarPorIdParaAlteracao(idDestino)
                .orElseThrow(() -> new ContaNaoEncontradaException(idDestino));

            origem.debitarTransferenciaEnviada(valor, destino);
            destino.creditarTransferenciaRecebida(valor, origem);

            contaRepositoryPort.salvar(origem);
            contaRepositoryPort.salvar(destino);

            Movimentacao movOrigem = origem.getMovimentacoes().getLast();
            Movimentacao movDestino = destino.getMovimentacoes().getLast();

            notificacaoPort.notificar(movOrigem, origem.getNumero());
            notificacaoPort.notificar(movDestino, destino.getNumero());
        });
    }
}
