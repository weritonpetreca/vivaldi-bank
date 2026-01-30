package com.vivaldibank.application.usecases;

import com.vivaldibank.domain.model.Conta;
import com.vivaldibank.domain.model.Cpf;
import com.vivaldibank.domain.model.exceptions.CpfJaCadastradoException;
import com.vivaldibank.domain.ports.in.CriarContaCommand;
import com.vivaldibank.domain.ports.out.ContaRepositoryPort;
import com.vivaldibank.domain.ports.out.NotificacaoPort;
import com.vivaldibank.domain.ports.out.PasswordEncoderPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CriarContaUseCaseTest {

    @Mock
    private ContaRepositoryPort contaRepositoryPort;

    @Mock
    private NotificacaoPort notificacaoPort;

    @Mock
    private PasswordEncoderPort passwordEncoder;

    @InjectMocks
    private CriarContaUseCase useCase;

    @Test
    @DisplayName("Deve lançar erro se CPF já estiver cadastrado")
    void deveFalharSeCpfExiste() {
        CriarContaCommand cmd = new CriarContaCommand("Dandelion","093.311.626-85", BigDecimal.TEN, "123");

        when(contaRepositoryPort.existePorCpf(any())).thenReturn(true);

        assertThrows(CpfJaCadastradoException.class, () -> useCase.executar(cmd));

        verify(contaRepositoryPort, never()).salvar((any()));
        verify(notificacaoPort, never()).notificarCriacao(any());
    }

    @Test
    @DisplayName("Deve criar conta e notificar se tudo estiver ok")
    void deveCriarContaComSucesso() {
        CriarContaCommand cmd = new CriarContaCommand(
            "Dandelion",
            "093.311.626-85",
            BigDecimal.TEN,
            "123");

        UUID idFixo = UUID.fromString("a6b56100-947a-44a7-a73c-f67a055c38ab");

        Conta contaSalvaMock = Conta.builder()
                .id(idFixo)
                .numero("20260001")
                .titularNome("Dandelion")
                .cpf(new Cpf("09331162685"))
                .saldo(BigDecimal.TEN)
                .criadoEm(LocalDateTime.now())
                .movimentacoes(new ArrayList<>())
                .senha("encodedPass")
                .role("USER")
                .build();


        when(passwordEncoder.encode("123")).thenReturn("encodedPass");
        when(contaRepositoryPort.existePorCpf(any())).thenReturn(false);

        when(contaRepositoryPort.salvar(any(Conta.class))).thenReturn(contaSalvaMock);

        Conta contaCriada = useCase.executar(cmd);

        assertNotNull(contaCriada);

        assertEquals(idFixo, contaCriada.getId());

        verify(contaRepositoryPort, times(1)).existePorCpf("09331162685");
        verify(contaRepositoryPort, times(1)).salvar(any(Conta.class));
        verify(notificacaoPort, times(1)).notificarCriacao(any(Conta.class));
    }
}
