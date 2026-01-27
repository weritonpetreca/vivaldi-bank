package com.vivaldibank;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.*;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@TestConfiguration(proxyBeanMethods = false)
class IntegrationTestConfig {

	@Bean
	@ServiceConnection
	PostgreSQLContainer<?> postgresContainer() {
		return new PostgreSQLContainer<>(DockerImageName.parse("postgres:16.1"))
				.withDatabaseName("vivaldibank-test")
				.withUsername("test")
				.withPassword("test");
	}

    @Bean
    @Primary
    public SqsAsyncClient sqsAsyncClient() {
        SqsAsyncClient mockClient = mock(SqsAsyncClient.class);

        when(mockClient.createQueue(any(CreateQueueRequest.class)))
            .thenReturn(CompletableFuture.completedFuture(
                CreateQueueResponse.builder()
                    .queueUrl("http://localhost:4566/000000000000/mock-queue")
                    .build()));

        when(mockClient.getQueueUrl(any(GetQueueUrlRequest.class)))
            .thenAnswer(invocation -> {
                GetQueueUrlRequest request = invocation.getArgument(0);
                return CompletableFuture.completedFuture(
                    GetQueueUrlResponse.builder()
                        .queueUrl("http://localhost:4566/000000000000/" + request.queueName())
                        .build()
                );
            });

        when(mockClient.sendMessage(any(SendMessageRequest.class)))
            .thenReturn(CompletableFuture.completedFuture(
                SendMessageResponse.builder()
                    .messageId(UUID.randomUUID().toString())
                    .build()));

        when(mockClient.receiveMessage(any(ReceiveMessageRequest.class)))
            .thenReturn(CompletableFuture.completedFuture(
                ReceiveMessageResponse.builder()
                    .build()));

        return mockClient;
    }

}
