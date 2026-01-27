package com.vivaldibank.infrastructure.config;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
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
        try {
            criarFila(properties.transacoes());
            criarFila(properties.clientes());
            criarFila(properties.login());
        } catch (Exception e) {
            logger.error("NÃO FOI POSSIVEL CONECTAR AO LOCALSTACK. " +
                "As filas não foram criadas. Erro: {}", e.getMessage());
        }
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
