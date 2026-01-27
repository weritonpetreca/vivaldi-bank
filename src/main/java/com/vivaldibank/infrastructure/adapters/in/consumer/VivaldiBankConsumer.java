package com.vivaldibank.infrastructure.adapters.in.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vivaldibank.infrastructure.events.EventoContaCriada;
import com.vivaldibank.infrastructure.events.EventoLogin;
import com.vivaldibank.infrastructure.events.EventoMovimentacao;
import io.awspring.cloud.sqs.annotation.SqsListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class VivaldiBankConsumer {

    private static final Logger logger = LoggerFactory.getLogger(VivaldiBankConsumer.class);

    @SqsListener("${vivaldi.queues.transacoes}")
    public void consumirTransacao(@Payload EventoMovimentacao evento) {
            logger.info("[Consumer] Recebida movimentação financeira: {} | Valor: {}", evento.tipo(), evento.valor());
    }

    @SqsListener("${vivaldi.queues.clientes}")
    public void consumirEventosClientes(@Payload EventoContaCriada evento) {
            logger.info("[Consumer Cliente] Bem-vindo(a) ao Vivaldi Bank: {}", evento.titular());
            // Lógica de envio de email aqui...
    }

    @SqsListener("${vivaldi.queues.login}")
    public void consumirEventosLogin(@Payload EventoLogin evento) {
            logger.info("[Consumer Security] Login auditado para conta ID: {}", evento.accountId());
            // Lógica de detecção de fraude ou geo-localização aqui...
    }
}
