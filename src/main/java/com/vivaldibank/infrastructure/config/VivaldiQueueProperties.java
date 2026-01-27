package com.vivaldibank.infrastructure.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "vivaldi.queues")
public record VivaldiQueueProperties(
    String transacoes,
    String clientes,
    String login
) {}
