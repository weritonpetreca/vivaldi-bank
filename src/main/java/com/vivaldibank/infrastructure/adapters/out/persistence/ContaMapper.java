package com.vivaldibank.infrastructure.adapters.out.persistence;

import com.vivaldibank.domain.model.Conta;
import com.vivaldibank.domain.model.Cpf;
import com.vivaldibank.domain.model.Movimentacao;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ContaMapper {

    public ContaEntity toEntity(Conta domain) {
        ContaEntity entity = new ContaEntity();
        entity.setId(domain.getId());
        entity.setNumero(domain.getNumero());
        entity.setTitularNome(domain.getTitularNome());
        entity.setCpf(domain.getCpf().getNumero());
        entity.setSaldo(domain.getSaldo());
        entity.setCriadoEm(domain.getCriadoEm());
        entity.setSenha(domain.getSenha());
        entity.setRole(domain.getRole());


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
                .toList();

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
                .toList();

        return Conta.builder()
                .id(entity.getId())
                .numero(entity.getNumero())
                .titularNome(entity.getTitularNome())
                .cpf(new Cpf(entity.getCpf()))
                .saldo(entity.getSaldo())
                .criadoEm(entity.getCriadoEm())
                .movimentacoes(movimentacoesDomain)
                .senha(entity.getSenha())
                .role(entity.getRole())
                .build();
    }
}
