package com.vivaldibank.domain.ports.out;

import com.vivaldibank.domain.model.Conta;

import java.util.Optional;

public interface ContaRepositoryPort {
    Conta salvar(Conta conta);
    Optional<Conta> buscarPorId(String id);
    Optional<Conta> buscarPorNumero(String numero);
    Optional<String> buscarUltimoNumeroConta();
}
