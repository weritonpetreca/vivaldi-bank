package com.vivaldibank.domain.ports.out;

public interface PasswordEncoderPort {
    String encode(String senhaPura);
    boolean matches(String senhaPura, String senhaHash);
}
