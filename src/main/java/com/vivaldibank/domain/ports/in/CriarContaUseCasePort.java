package com.vivaldibank.domain.ports.in;

import com.vivaldibank.domain.model.Conta;

public interface CriarContaUseCasePort {

    /**
     * Executa a lógica de criação conta
     * @param command Os dados de entrada validados
     * @return A Conta criada (o domínio devolve o objeto rico)
     */

    Conta executar(CriarContaCommand command);
}
