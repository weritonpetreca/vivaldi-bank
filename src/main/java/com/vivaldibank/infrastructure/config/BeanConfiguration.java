package com.vivaldibank.infrastructure.config;

import com.vivaldibank.application.usecases.*;
import com.vivaldibank.domain.ports.out.ContaRepositoryPort;
import com.vivaldibank.domain.ports.out.NotificacaoPort;
import com.vivaldibank.domain.ports.out.PasswordEncoderPort;
import com.vivaldibank.domain.ports.out.TransactionalPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

@Configuration
public class BeanConfiguration {

    @Bean
    public TransactionTemplate transactionTemplate(PlatformTransactionManager transactionManager) {
        return new TransactionTemplate(transactionManager);
    }

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
            NotificacaoPort notificacaoPort,
            PasswordEncoderPort passwordEncoderPort) {
        return new CriarContaUseCase(contaRepositoryPort, notificacaoPort, passwordEncoderPort);
    }

    @Bean
    public RealizarDepositoUseCase realizarDepositoUseCase(
            ContaRepositoryPort contaRepositoryPort,
            NotificacaoPort notificacaoPort,
            TransactionalPort transactionalPort
        ) {
        return new RealizarDepositoUseCase(contaRepositoryPort, notificacaoPort, transactionalPort);
    }

    @Bean
    public RealizarSaqueUseCase realizarSaqueUseCase(
            ContaRepositoryPort contaRepositoryPort,
            NotificacaoPort notificacaoPort,
            TransactionalPort transactionalPort) {
        return new RealizarSaqueUseCase(contaRepositoryPort, notificacaoPort, transactionalPort);
    }

    @Bean
    public RealizarTransferenciaUseCase realizarTransferenciaUseCase(
            ContaRepositoryPort contaRepositoryPort,
            NotificacaoPort notificacaoPort,
            TransactionalPort transactionalPort) {
        return new RealizarTransferenciaUseCase(contaRepositoryPort, notificacaoPort, transactionalPort);
    }
}
