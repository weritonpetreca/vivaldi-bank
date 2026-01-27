package com.vivaldibank.infrastructure.security;

import com.vivaldibank.infrastructure.adapters.out.persistence.ContaEntity;
import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.List;
import java.util.UUID;

@Getter
public class SecurityUser extends User {

    private final UUID idConta;

    public SecurityUser(ContaEntity conta) {
        super(
            conta.getCpf(),
            conta.getSenha(),
            List.of(new SimpleGrantedAuthority("ROLE_" + conta.getRole()))
        );
        this.idConta = conta.getId();
    }
}
