package com.vivaldibank.application.usecases;

import com.vivaldibank.domain.model.exceptions.ContaNaoEncontradaException;
import com.vivaldibank.domain.model.Conta;
import com.vivaldibank.domain.ports.out.ContaRepositoryPort;

public class BuscarContaPorIdUseCase {

    private final ContaRepositoryPort contaRepositoryPort;

    public BuscarContaPorIdUseCase(ContaRepositoryPort contaRepositoryPort) {
        this.contaRepositoryPort = contaRepositoryPort;
    }

    public Conta buscar(String id) {
        return contaRepositoryPort.buscarPorId(id)
                .orElseThrow(() -> new ContaNaoEncontradaException(id));
    }
}
