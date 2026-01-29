package com.vivaldibank.domain.model.exceptions;

public class ContaNaoEncontradaException extends RuntimeException {

    public ContaNaoEncontradaException(String id) {
        super("Conta não encontrada com o ID: " + id);
    }
}
