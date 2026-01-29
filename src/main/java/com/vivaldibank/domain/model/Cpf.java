package com.vivaldibank.domain.model;

import com.vivaldibank.domain.model.exceptions.CpfInvalidoException;

public class Cpf {
    private final String numero;

    public Cpf(String numero) {
        if (!CpfValidator.isValid(numero)) {
            throw new CpfInvalidoException(numero);
        } else {
            this.numero = numero.replaceAll("\\D", "");
        }
    }

    public String getNumero() {
        return numero;
    }

    @Override
    public String toString() {
        return numero.replaceAll("(\\d{3})(\\d{3})(\\d{3})(\\d{2})", "$1.$2.$3-$4");
    }
}
