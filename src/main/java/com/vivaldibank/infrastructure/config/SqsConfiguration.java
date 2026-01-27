package com.vivaldibank.infrastructure.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.awspring.cloud.sqs.config.SqsMessageListenerContainerFactory;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import io.awspring.cloud.sqs.support.converter.SqsMessagingMessageConverter;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.CreateQueueRequest;
import software.amazon.awssdk.services.sqs.model.QueueNameExistsException;

@Configuration
public class SqsConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(SqsConfiguration.class);

    private final SqsAsyncClient sqsAsyncClient;
    private final VivaldiQueueProperties properties;

    public SqsConfiguration(SqsAsyncClient sqsAsyncClient,
                            VivaldiQueueProperties properties) {
        this.sqsAsyncClient = sqsAsyncClient;
        this.properties = properties;
    }

    @PostConstruct
    public void criarFilasAoIniciar() {
        logger.info("Verificando existência das filas SQS...");
        criarFila(properties.transacoes());
        criarFila(properties.clientes());
        criarFila(properties.login());
    }


    private void criarFila(String nomeFila) {
        try {
            CreateQueueRequest createRequest = CreateQueueRequest.builder().queueName(nomeFila).build();

            sqsAsyncClient.createQueue(createRequest).join();

            logger.info("Fila criada com sucesso: {}", nomeFila);
        } catch (Exception e) {
            if (e.getCause() instanceof QueueNameExistsException) {
                logger.info("A fila {} já existe. Nenhuma ação necessária.", nomeFila);
            } else {
                logger.error("Erro ao tentar criar fila '{}': {}", nomeFila, e.getMessage());
            }
        }
    }
}
