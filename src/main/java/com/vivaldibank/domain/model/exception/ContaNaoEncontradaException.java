package com.vivaldibank.domain.model.exception;

public class ContaNaoEncontradaException extends RuntimeException {

    public ContaNaoEncontradaException(String id) {
        super("Conta não encontrada com o ID: " + id);
    }
}
