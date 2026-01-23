package com.vivaldibank.infrastructure.adapters.out.persistence;

import com.vivaldibank.domain.model.Conta;
import com.vivaldibank.domain.model.Movimentacao;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ContaMapper {

    public ContaEntity toEntity(Conta domain) {
        ContaEntity entity = new ContaEntity();
        entity.setId(domain.getId());
        entity.setNumero(domain.getNumero());
        entity.setTitularNome(domain.getTitularNome());
        entity.setCpf(domain.getCpf());
        entity.setSaldo(domain.getSaldo());
        entity.setCriadoEm(domain.getCriadoEm());

        List<MovimentacaoEntity> movimentacoesEntity = domain.getMovimentacoes().stream()
                .map(m -> new MovimentacaoEntity(
                        m.getId(),
                        m.getDataHora(),
                        m.getValor(),
                        m.getTipo(),
                        m.getContraparteNome(),
                        m.getContraparteNumeroConta(),
                        entity
                ))
                .collect(Collectors.toList());

        entity.setMovimentacoes(movimentacoesEntity);

        return entity;
    }

    public Conta toDomain(ContaEntity entity) {
        List<Movimentacao> movimentacoesDomain = entity.getMovimentacoes().stream()
                .map(m -> new Movimentacao(
                        m.getId(),
                        m.getDataHora(),
                        m.getValor(),
                        m.getTipo(),
                        m.getContraparteNome(),
                        m.getContraparteNumeroConta()
                ))
                .collect(Collectors.toList());

        return new Conta(
                entity.getId(),
                entity.getNumero(),
                entity.getTitularNome(),
                entity.getCpf(),
                entity.getSaldo(),
                entity.getCriadoEm(),
                movimentacoesDomain
        );
    }
}
