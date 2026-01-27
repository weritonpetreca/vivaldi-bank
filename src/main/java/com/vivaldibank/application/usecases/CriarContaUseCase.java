package com.vivaldibank.application.usecases;

import com.vivaldibank.domain.model.Conta;
import com.vivaldibank.domain.model.exception.CpfJaCadastradoException;
import com.vivaldibank.domain.ports.in.CriarContaCommand;
import com.vivaldibank.domain.ports.in.CriarContaUseCasePort;
import com.vivaldibank.domain.ports.out.ContaRepositoryPort;
import com.vivaldibank.domain.ports.out.NotificacaoPort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.math.BigDecimal;
import java.time.Year;
import java.util.Optional;

public class CriarContaUseCase implements CriarContaUseCasePort {

    private final ContaRepositoryPort contaRepository;
    private final NotificacaoPort notificacaoPort;

    public CriarContaUseCase(ContaRepositoryPort contaRepository, NotificacaoPort notificacaoPort) {
        this.contaRepository = contaRepository;
        this.notificacaoPort = notificacaoPort;
    }

    @Override
    public Conta executar(CriarContaCommand command) {

        if (contaRepository.existePorCpf(command.cpf())) {
            throw new CpfJaCadastradoException(command.cpf());
        }

        String novoNumero = gerarProximoNumero();

        String senhaCriptografada = new BCryptPasswordEncoder().encode(command.senha());

        Conta novaConta = new Conta(
            novoNumero,
            command.titularNome(),
            command.cpf(),
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
            return String.format("%d%04d", anoAtual, 1);
        }

        String ultimoNumero = ultimoNumeroOpt.get();

        int ultimoAno = Integer.parseInt(ultimoNumero.substring(0,4));
        int seqUltimaConta = Integer.parseInt(ultimoNumero.substring(4));

        if (anoAtual > ultimoAno) {
            return String.format("%d%04d", anoAtual, 1);
        }

        return String.format("%d%04d", anoAtual, seqUltimaConta + 1);
    }
}
