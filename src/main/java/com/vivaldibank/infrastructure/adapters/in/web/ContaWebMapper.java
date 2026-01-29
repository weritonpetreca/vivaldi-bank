package com.vivaldibank.infrastructure.adapters.in.web;

import com.vivaldibank.domain.model.Conta;
import com.vivaldibank.infrastructure.adapters.in.web.dto.ContaCriadaResponse;
import com.vivaldibank.infrastructure.adapters.in.web.dto.ContaResponse;
import com.vivaldibank.infrastructure.adapters.in.web.dto.MovimentacaoResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ContaWebMapper {

    public ContaResponse toResponse(Conta conta) {
        List<MovimentacaoResponse> movimentacoes = conta.getMovimentacoes().stream()
                .map(m -> new MovimentacaoResponse(
                        m.getId().toString(),
                        m.getDataHora().toString(),
                        m.getValor(),
                        m.getTipo().name(),
                        m.getContraparteNome(),
                        m.getContraparteNumeroConta()
                ))
                .toList();

        return new ContaResponse(
                conta.getId() != null ? conta.getId().toString() : null,
                conta.getNumero(),
                conta.getTitularNome(),
                conta.getCpf().getNumero(),
                conta.getSaldo(),
                conta.getCriadoEm(),
                movimentacoes
        );
    }

    public ContaCriadaResponse toCriadaResponse(Conta conta, String token) {
        return new ContaCriadaResponse(
            conta.getId(),
            conta.getNumero(),
            conta.getTitularNome(),
            conta.getSaldo(),
            token
        );
    }
}
