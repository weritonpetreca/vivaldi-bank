package com.vivaldibank.application.usecases;

import com.vivaldibank.domain.model.Conta;
import com.vivaldibank.domain.model.exception.ContaNaoEncontradaException;
import com.vivaldibank.domain.ports.out.ContaRepositoryPort;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

public class RealizarDepositoUseCase {

    private final ContaRepositoryPort contaRepositoryPort;

    public RealizarDepositoUseCase(ContaRepositoryPort contaRepositoryPort) {
        this.contaRepositoryPort = contaRepositoryPort;
    }

    @Transactional
    public Conta executar(String id, BigDecimal valor) {

        Conta conta = contaRepositoryPort.buscarPorIdParaAlteracao(id)
                .orElseThrow(() -> new ContaNaoEncontradaException(id));

        conta.depositar(valor);

        return contaRepositoryPort.salvar(conta);
    }


}
