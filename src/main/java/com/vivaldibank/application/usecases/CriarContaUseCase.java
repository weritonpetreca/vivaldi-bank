package com.vivaldibank.application.usecases;

import com.vivaldibank.domain.model.Conta;
import com.vivaldibank.domain.model.exception.CpfJaCadastradoException;
import com.vivaldibank.domain.ports.in.CriarContaCommand;
import com.vivaldibank.domain.ports.in.CriarContaUseCasePort;
import com.vivaldibank.domain.ports.out.ContaRepositoryPort;
import com.vivaldibank.domain.ports.out.NotificacaoPort;
import com.vivaldibank.domain.ports.out.PasswordEncoderPort;

import java.math.BigDecimal;
import java.time.Year;
import java.util.Optional;

public class CriarContaUseCase implements CriarContaUseCasePort {

    public static final String STRING_FORMATACAO = "%d%04d";
    private final ContaRepositoryPort contaRepository;
    private final NotificacaoPort notificacaoPort;
    private final PasswordEncoderPort passwordEncoderPort;

    public CriarContaUseCase(ContaRepositoryPort contaRepository,
                             NotificacaoPort notificacaoPort,
                             PasswordEncoderPort passwordEncoderPort) {
        this.contaRepository = contaRepository;
        this.notificacaoPort = notificacaoPort;
        this.passwordEncoderPort = passwordEncoderPort;
    }

    @Override
    public Conta executar(CriarContaCommand command) {

        String cpfLimpo = command.cpf().replaceAll("\\D", "");

        if (contaRepository.existePorCpf(cpfLimpo)) {
            throw new CpfJaCadastradoException(cpfLimpo);
        }

        String novoNumero = gerarProximoNumero();

        String senhaCriptografada = passwordEncoderPort.encode(command.senha());

        Conta novaConta = new Conta(
            novoNumero,
            command.titularNome(),
            cpfLimpo,
            senhaCriptografada
        );

        if (command.depositoInicial() != null && command.depositoInicial().compareTo(BigDecimal.ZERO) > 0) {
            novaConta.depositar(command.depositoInicial());
        }

        Conta contaSalva = contaRepository.salvar(novaConta);

        notificacaoPort.notificarCriacao(contaSalva);

        return contaSalva;
    }

    private String gerarProximoNumero() {
        Optional<String> ultimoNumeroOpt = contaRepository.buscarUltimoNumeroConta();

        int anoAtual = Year.now().getValue();

        if (ultimoNumeroOpt.isEmpty()) {
            return String.format(STRING_FORMATACAO, anoAtual, 1);
        }

        String ultimoNumero = ultimoNumeroOpt.get();

        int ultimoAno = Integer.parseInt(ultimoNumero.substring(0,4));
        int seqUltimaConta = Integer.parseInt(ultimoNumero.substring(4));

        if (anoAtual > ultimoAno) {
            return String.format(STRING_FORMATACAO, anoAtual, 1);
        }

        return String.format(STRING_FORMATACAO, anoAtual, seqUltimaConta + 1);
    }
}
