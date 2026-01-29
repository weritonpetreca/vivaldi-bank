package com.vivaldibank.domain.model.exceptions;

public class CpfInvalidoException extends RuntimeException {
    public CpfInvalidoException(String cpfInvalido) {
        super("O CPF inserido é inválido: " + cpfInvalido);
    }
}
