package com.vivaldibank.domain.model.exceptions;

public class CpfJaCadastradoException extends RuntimeException {
    public CpfJaCadastradoException(String cpf) {
        super("O CPF " + cpf + " já está cadastrado no sistema.");
    }
}
