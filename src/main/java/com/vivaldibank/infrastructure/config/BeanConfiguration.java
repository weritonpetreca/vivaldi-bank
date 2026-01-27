package com.vivaldibank.infrastructure.config;

import com.vivaldibank.application.usecases.*;
import com.vivaldibank.domain.ports.out.ContaRepositoryPort;
import com.vivaldibank.domain.ports.out.NotificacaoPort;
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
    public CriarContaUseCase criarContaUseCase(
            ContaRepositoryPort contaRepositoryPort,
            NotificacaoPort notificacaoPort) {
        return new CriarContaUseCase(contaRepositoryPort, notificacaoPort);
    }

    @Bean
    public RealizarDepositoUseCase realizarDepositoUseCase(
            ContaRepositoryPort contaRepositoryPort,
            NotificacaoPort notificacaoPort
        ) {
        return new RealizarDepositoUseCase(contaRepositoryPort, notificacaoPort);
    }

    @Bean
    public RealizarSaqueUseCase realizarSaqueUseCase(
            ContaRepositoryPort contaRepositoryPort,
            NotificacaoPort notificacaoPort) {
        return new RealizarSaqueUseCase(contaRepositoryPort, notificacaoPort);
    }

    @Bean
    public RealizarTransferenciaUseCase realizarTransferenciaUseCase(
            ContaRepositoryPort contaRepositoryPort,
            NotificacaoPort notificacaoPort) {
        return new RealizarTransferenciaUseCase(contaRepositoryPort, notificacaoPort);
    }
}
