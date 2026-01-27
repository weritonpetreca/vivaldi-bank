package com.vivaldibank.infrastructure.adapters.out.notification;

import com.vivaldibank.domain.model.Conta;
import com.vivaldibank.domain.model.Movimentacao;
import com.vivaldibank.domain.model.TipoEvento;
import com.vivaldibank.domain.ports.out.NotificacaoPort;
import com.vivaldibank.infrastructure.config.VivaldiQueueProperties;
import com.vivaldibank.infrastructure.events.EventoContaCriada;
import com.vivaldibank.infrastructure.events.EventoLogin;
import com.vivaldibank.infrastructure.events.EventoMovimentacao;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;

@Component
public class SqsNotificacaoAdapter implements NotificacaoPort {

    private static final Logger logger = LoggerFactory.getLogger(SqsNotificacaoAdapter.class);

    private final SqsTemplate sqsTemplate;
    private final VivaldiQueueProperties properties;

    public SqsNotificacaoAdapter(SqsTemplate sqsTemplate,
                                 VivaldiQueueProperties properties) {
        this.sqsTemplate = sqsTemplate;
        this.properties = properties;
    }

    @Override
    public void notificar(Movimentacao movimentacao, String numeroContaOrigem) {

        EventoMovimentacao evento = new EventoMovimentacao(
            movimentacao.getId().toString(),
            movimentacao.getDataHora().format(DateTimeFormatter.ISO_DATE_TIME),
            numeroContaOrigem,
            movimentacao.getValor(),
            movimentacao.getTipo().name(),
            movimentacao.getContraparteNumeroConta()
        );

        enviarParaFila(properties.transacoes(), evento, evento.tipo());
    }

    @Override
    public void notificarCriacao(Conta conta) {
        EventoContaCriada evento = new EventoContaCriada(
            conta.getId().toString(),
            conta.getNumero(),
            conta.getTitularNome(),
            TipoEvento.CONTA_CRIADA.name()
        );

        enviarParaFila(properties.clientes(), evento, "CONTA_CRIADA");
    }

    @Override
    public void notificarLogin(String idConta, String cpf) {
        String cpfMascarado = mascararCpf(cpf);

        EventoLogin evento = new EventoLogin(
            idConta,
            cpfMascarado,
            TipoEvento.LOGIN_REALIZADO.name());

        enviarParaFila(properties.login(), evento, "LOGIN_REALIZADO");
    }

    private String mascararCpf(String cpf) {
        if (cpf == null || cpf.length() != 11) {
            return "UNKNOWN_USER";
        }
        return cpf.substring(0, 3) + ".***.***-" + cpf.substring(9);
    }

    private void enviarParaFila(String fila, Object payload, String tipoEventoLog) {

            logger.info("Enviando evento [{}] para fila [{}]", tipoEventoLog, fila);

            sqsTemplate.send(to -> to
                .queue(fila)
                .payload(payload)
            );

            logger.info("Evento [{}] enviado para fila [{}]: {}", tipoEventoLog, fila, payload);
    }
}
