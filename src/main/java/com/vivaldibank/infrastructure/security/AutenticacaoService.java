package com.vivaldibank.infrastructure.security;

import com.vivaldibank.infrastructure.adapters.out.persistence.SpringDataContaRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AutenticacaoService implements UserDetailsService {

    // Usamos o Repositório do Spring direto aqui pois é uma classe de Infrasetrutura
    private final SpringDataContaRepository repository;

    public AutenticacaoService(SpringDataContaRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String cpf) throws UsernameNotFoundException {
        // 1. Busca a conta no banco pelo CPF
        // O CPF que chega aqui vem do formulário de login
        var contaEntity = repository.findByCpf(cpf)
            .orElseThrow(() -> new UsernameNotFoundException("Conta não encontrada com CPF: " + cpf));

        // 2. Transforma a ContaEntity em um User do Spring Security
        // O Spring vai usar essa senha e role para checar se o login está certo
        return User.builder()
            .username(contaEntity.getCpf())
            .password(contaEntity.getSenha()) // O Spring compara esse Hash com a senha enviada
            .roles(contaEntity.getRole())     // Ex: "CLIENTE" vira "ROLE_CLIENTE"
            .build();
    }
}
