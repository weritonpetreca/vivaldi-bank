package com.vivaldibank.infrastructure.adapters.out.security;

import com.vivaldibank.domain.ports.out.PasswordEncoderPort;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class BCryptPasswordEncoderAdapter implements PasswordEncoderPort {

    private final PasswordEncoder springPasswordEncoder;

    @Override
    public String encode(String senhaPura) {
        return springPasswordEncoder.encode(senhaPura);
    }

    @Override
    public boolean matches(String senhaPura, String senhaHash) {
        return springPasswordEncoder.matches(senhaPura, senhaHash);
    }

}
