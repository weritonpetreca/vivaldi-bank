package com.vivaldibank.domain.model;

public class CpfValidator {

    public static boolean isValid(String cpf) {
        if (cpf == null) {
            return false;
        }

        // Remove caracteres não numéricos
        String cpfNumerico = cpf.replaceAll("\\D", "");

        // Verifica se tem 11 dígitos
        if (cpfNumerico.length() != 11) {
            return false;
        }

        // Verifica se todos os dígitos são iguais (ex: 111.111.111-11)
        if (cpfNumerico.matches("(\\d)\\1{10}")) {
            return false;
        }

        try {
            // Cálculo do primeiro dígito verificador
            int soma = 0;
            int peso = 10;
            for (int i = 0; i < 9; i++) {
                int num = (int) (cpfNumerico.charAt(i) - 48);
                soma = soma + (num * peso);
                peso = peso - 1;
            }

            int r = 11 - (soma % 11);
            char dig10;
            if ((r == 10) || (r == 11)) {
                dig10 = '0';
            } else {
                dig10 = (char) (r + 48);
            }

            // Cálculo do segundo dígito verificador
            soma = 0;
            peso = 11;
            for (int i = 0; i < 10; i++) {
                int num = (int) (cpfNumerico.charAt(i) - 48);
                soma = soma + (num * peso);
                peso = peso - 1;
            }

            r = 11 - (soma % 11);
            char dig11;
            if ((r == 10) || (r == 11)) {
                dig11 = '0';
            } else {
                dig11 = (char) (r + 48);
            }

            // Verifica se os dígitos calculados conferem com os dígitos informados
            return (dig10 == cpfNumerico.charAt(9)) && (dig11 == cpfNumerico.charAt(10));

        } catch (Exception e) {
            return false;
        }
    }
}
