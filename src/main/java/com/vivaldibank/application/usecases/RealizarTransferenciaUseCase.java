package com.vivaldibank.application.usecases;

import com.vivaldibank.domain.model.Conta;
import com.vivaldibank.domain.model.exception.ContaNaoEncontradaException;
import com.vivaldibank.domain.ports.out.ContaRepositoryPort;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

public class RealizarTransferenciaUseCase {

    private final ContaRepositoryPort contaRepositoryPort;

    public RealizarTransferenciaUseCase(ContaRepositoryPort contaRepositoryPort) {
        this.contaRepositoryPort = contaRepositoryPort;
    }

    @Transactional
    public void executar(String idOrigem, String idDestino, BigDecimal valor) {

        if (idOrigem.equals(idDestino)) {
            throw new IllegalArgumentException("Não é possível transferir para a mesma conta");
        }

        Conta origem = contaRepositoryPort.buscarPorId(idOrigem)
                .orElseThrow(() -> new ContaNaoEncontradaException(idOrigem));

        Conta destino = contaRepositoryPort.buscarPorId(idDestino)
                .orElseThrow(() -> new ContaNaoEncontradaException(idDestino));

        origem.debitarTransferenciaEnviada(valor, destino);
        destino.creditarTransferenciaRecebida(valor, origem);

        contaRepositoryPort.salvar(origem);
        contaRepositoryPort.salvar(destino);
    }
}
