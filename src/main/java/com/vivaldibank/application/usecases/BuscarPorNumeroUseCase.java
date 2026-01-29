package com.vivaldibank.application.usecases;

import com.vivaldibank.domain.model.Conta;
import com.vivaldibank.domain.model.exceptions.NumeroNaoEncontradoException;
import com.vivaldibank.domain.ports.out.ContaRepositoryPort;

public class BuscarPorNumeroUseCase {

    private final ContaRepositoryPort contaRepositoryPort;

    public BuscarPorNumeroUseCase(ContaRepositoryPort contaRepositoryPort) {
        this.contaRepositoryPort = contaRepositoryPort;
    }

    public Conta buscar(String numero) {
        return contaRepositoryPort.buscarPorNumero(numero)
                .orElseThrow(() -> new NumeroNaoEncontradoException(numero));
    }
}
