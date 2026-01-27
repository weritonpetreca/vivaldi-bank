package com.vivaldibank.domain.ports.out;

import com.vivaldibank.domain.model.Conta;
import com.vivaldibank.domain.model.Movimentacao;

public interface NotificacaoPort {

    void notificar(Movimentacao movimentacao, String numeroContaOrigem);

    void notificarCriacao(Conta conta);

    void notificarLogin(String idConta, String cpf);
}
