package com.vivaldibank.infrastructure.adapters.out.persistence;

import com.vivaldibank.domain.model.Conta;
import com.vivaldibank.domain.model.Movimentacao;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContaRepositoryAdapterTest {

    @Mock
    private SpringDataContaRepository springRepository;

    @Mock
    private ContaMapper contaMapper;

    @InjectMocks
    private ContaRepositoryAdapter adapter;

    @Test
    @DisplayName("Deve salvar uma conta com sucesso")
    void deveSalvarConta() {
        Conta contaDominio = new Conta("12345", "João Silva", "09331162685", "senha123");
        ContaEntity contaEntity = new ContaEntity(UUID.randomUUID(), "12345", BigDecimal.ZERO, "João Silva", "09331162685", LocalDateTime.now(), "senha123", "USER", new ArrayList<>());

        when(contaMapper.toEntity(contaDominio)).thenReturn(contaEntity);
        when(springRepository.save(contaEntity)).thenReturn(contaEntity);
        when(contaMapper.toDomain(contaEntity)).thenReturn(contaDominio);

        Conta resultado = adapter.salvar(contaDominio);

        assertNotNull(resultado);
        assertEquals(contaDominio.getNumero(), resultado.getNumero());

        verify(contaMapper).toEntity(contaDominio);
        verify(springRepository).save(contaEntity);
        verify(contaMapper).toDomain(contaEntity);
    }
}
