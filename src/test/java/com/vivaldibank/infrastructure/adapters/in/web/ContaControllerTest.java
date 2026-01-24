package com.vivaldibank.infrastructure.adapters.in.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vivaldibank.application.usecases.*;
import com.vivaldibank.domain.model.Conta;
import com.vivaldibank.domain.ports.in.CriarContaCommand;
import com.vivaldibank.infrastructure.adapters.in.web.dto.CriarContaRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ContaController.class)
@Import(ContaWebMapper.class)
@AutoConfigureMockMvc(addFilters = false)
class ContaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CriarContaUseCase criarContaUseCase;

    @MockitoBean
    private BuscarContaPorIdUseCase buscarContaPorIdUseCase;

    @MockitoBean
    private BuscarPorNumeroUseCase buscarPorNumeroUseCase;

    @MockitoBean
    private RealizarDepositoUseCase realizarDepositoUseCase;

    @MockitoBean
    private RealizarSaqueUseCase realizarSaqueUseCase;

    @MockitoBean
    private RealizarTransferenciaUseCase realizarTransferenciaUseCase;

    @Test
    @DisplayName("Deve criar uma conta com sucesso e retornar status 201")
    void deveCriarContaComSucesso() throws Exception {

        CriarContaRequest request = new CriarContaRequest("Geralt de Rivia", "09331162685", BigDecimal.ZERO, "senha123");
        Conta contaCriada = new Conta("20260001", "Geralt de Rivia", "09331162685", "senha123");

        when(criarContaUseCase.executar(any(CriarContaCommand.class))).thenReturn(contaCriada);

        mockMvc.perform(post("/contas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.numero").value("20260001"))
                .andExpect(jsonPath("$.titularNome").value("Geralt de Rivia"))
                .andExpect(jsonPath("$.saldo").value(0));
    }

    @Test
    @DisplayName("Deve retornar 400 Bad Request quando dados de entrada forem inválidos")
    void deveRetornarErroQuandoDadosInvalidos() throws Exception {

        CriarContaRequest requestInvalido = new CriarContaRequest("", "", BigDecimal.ZERO, "senha123");

        mockMvc.perform(post("/contas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestInvalido)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Erro de Validação de Dados"))
                .andExpect(jsonPath("$.detail").value("Dados de entrada inválidos"));
    }

    @Test
    @DisplayName("Deve retornar 422 Unprocessable Entity quando regra de negócio falhar")
    void deveRetornarErroQuandoRegraNegocioFalhar() throws Exception {

        CriarContaRequest request = new CriarContaRequest("Geralt de Rivia", "00000000000", BigDecimal.ZERO, "senha123");

        when(criarContaUseCase.executar(any(CriarContaCommand.class)))
                .thenThrow(new IllegalArgumentException("CPF inválido"));

        mockMvc.perform(post("/contas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.title").value("Erro de Regra de Negócio"))
                .andExpect(jsonPath("$.detail").value("CPF inválido"));
    }
}
