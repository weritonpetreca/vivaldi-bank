package com.vivaldibank.infrastructure.adapters.in.web;

import com.vivaldibank.application.usecases.*;
import com.vivaldibank.domain.model.Conta;
import com.vivaldibank.domain.ports.in.CriarContaCommand;
import com.vivaldibank.infrastructure.adapters.in.web.dto.*;
import com.vivaldibank.infrastructure.security.TokenService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.math.BigDecimal;
import java.net.URI;

@RestController
@RequestMapping("/contas")
@RequiredArgsConstructor
public class ContaController {

    private final BuscarContaPorIdUseCase buscarContaPorIdUseCase;
    private final BuscarPorNumeroUseCase buscarPorNumeroUseCase;
    private final CriarContaUseCase criarContaUseCase;
    private final ContaWebMapper contaWebMapper;
    private final RealizarDepositoUseCase realizarDepositoUseCase;
    private final RealizarSaqueUseCase realizarSaqueUseCase;
    private final RealizarTransferenciaUseCase realizarTransferenciaUseCase;
    private final TokenService tokenService;

    @PostMapping
    @Operation(summary = "Criar nova conta", description = "Criar uma conta e já retorna o Token de acesso (Auto-Login.")
    public ResponseEntity<ContaCriadaResponse> criarConta(@RequestBody @Valid CriarContaRequest request) {

        BigDecimal valorInicial = request.depositoInicial() != null
                ? request.depositoInicial()
                : BigDecimal.ZERO;

        CriarContaCommand command = new CriarContaCommand(
                request.nomeTitular(),
                request.cpf(),
                valorInicial,
                request.senha()
        );

        Conta novaConta = criarContaUseCase.executar(command);

        String token = tokenService.gerarToken(novaConta.getCpf());

        ContaCriadaResponse response = contaWebMapper.toCriadaResponse(novaConta, token);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(novaConta.getId())
            .toUri();

        return ResponseEntity.created(location).body(response);
    }

    @PostMapping("/{id}/deposito")
    @Operation(summary = "Realizar depósito", description = "Creditar um valor na conta.")
    public ResponseEntity<Void> depositar(
            @PathVariable String id,
            @RequestBody @Valid DepositoRequest request) {

        realizarDepositoUseCase.executar(id, request.valor());

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/saque")
    @Operation(summary = "Realizar saque", description = "Debita um valor da conta, se houver saldo suficiente.")
    public ResponseEntity<Void> sacar(
            @PathVariable String id,
            @RequestBody @Valid SaqueRequest request) {

        realizarSaqueUseCase.executar(id, request.valor());

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{idOrigem}/transferencia")
    @Operation(summary = "Realizar transferência", description = "Transferir um valor de uma conta para outra.")
    public ResponseEntity<Void> transferir(
            @PathVariable String idOrigem,
            @RequestBody @Valid TransferenciaRequest request) {

        realizarTransferenciaUseCase.executar(idOrigem,
                request.idDestino(),
                request.valor());

        return ResponseEntity.noContent().build();
    }


    @GetMapping("/{id}")
    @Operation(summary = "Buscar conta por ID", description = "Buscar uma conta bancária por ID.")
    public ResponseEntity<ContaResponse> buscarPorId(@PathVariable String id) {

        Conta conta = buscarContaPorIdUseCase.buscar(id);

        ContaResponse response = contaWebMapper.toResponse(conta);

        return ResponseEntity.ok(response);
    }

    @GetMapping(params = "numero")
    @Operation(summary = "Buscar conta por número de conta", description = "Buscar uma conta bancária por número.")
    public ResponseEntity<ContaResponse> buscarPorNumero(@RequestParam String numero) {

        Conta conta = buscarPorNumeroUseCase.buscar(numero);

        ContaResponse response = contaWebMapper.toResponse(conta);

        return ResponseEntity.ok(response);
    }
}
