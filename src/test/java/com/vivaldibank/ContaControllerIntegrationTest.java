package com.vivaldibank;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vivaldibank.infrastructure.adapters.in.web.dto.CriarContaRequest;
import com.vivaldibank.infrastructure.adapters.out.persistence.SpringDataContaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc(addFilters = false)
@Import(IntegrationTestConfig.class)
@Transactional
class ContaControllerIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private SpringDataContaRepository springDataContaRepository;

	@Test
	@DisplayName("Deve criar conta e persistir no banco de dados PostgrSQL real")
	void deveCriarContaEndToEnd() throws Exception {

		CriarContaRequest request = new CriarContaRequest(
				"Vesemir",
				"778.253.010-56",
				new BigDecimal("1000.00")
		);

		mockMvc.perform(post("/contas")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.id").exists())
				.andExpect(jsonPath("$.titularNome").value("Vesemir"));

		var contaSalva = springDataContaRepository.findByCpf("77825301056")
				.orElseThrow(() -> new AssertionError("Conta não encontrada no banco!"));

		assertEquals("Vesemir", contaSalva.getTitularNome());
		assertEquals("77825301056", contaSalva.getCpf());
		assertEquals(0,new BigDecimal("1000.00").compareTo(contaSalva.getSaldo()));
	}

}

