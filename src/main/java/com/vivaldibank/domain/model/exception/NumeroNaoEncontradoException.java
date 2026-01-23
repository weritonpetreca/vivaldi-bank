package com.vivaldibank.domain.model.exception;

public class NumeroNaoEncontradoException extends RuntimeException {

    public NumeroNaoEncontradoException(String numero) {
        super("Número da conta não encontrado: " + numero);
    }
}
