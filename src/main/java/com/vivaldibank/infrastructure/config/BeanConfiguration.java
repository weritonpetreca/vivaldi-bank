package com.vivaldibank.infrastructure.config;

import com.vivaldibank.application.usecases.*;
import com.vivaldibank.domain.ports.out.ContaRepositoryPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {

    @Bean
    public BuscarContaPorIdUseCase buscarContaPorIdUseCase(ContaRepositoryPort contaRepositoryPort) {
        return new BuscarContaPorIdUseCase(contaRepositoryPort);
    }

    @Bean
    public BuscarPorNumeroUseCase buscarPorNumeroUseCase(ContaRepositoryPort contaRepositoryPort) {
        return new BuscarPorNumeroUseCase(contaRepositoryPort);
    }

    @Bean
    public CriarContaUseCase criarContaUseCase(ContaRepositoryPort contaRepositoryPort) {
        return new CriarContaUseCase(contaRepositoryPort);
    }

    @Bean
    public RealizarDepositoUseCase realizarDepositoUseCase(ContaRepositoryPort contaRepositoryPort) {
        return new RealizarDepositoUseCase(contaRepositoryPort);
    }

    @Bean
    public RealizarSaqueUseCase realizarSaqueUseCase(ContaRepositoryPort contaRepositoryPort) {
        return new RealizarSaqueUseCase(contaRepositoryPort);
    }

    @Bean
    public RealizarTransferenciaUseCase realizarTransferenciaUseCase(ContaRepositoryPort contaRepositoryPort) {
        return new RealizarTransferenciaUseCase(contaRepositoryPort);
    }
}
