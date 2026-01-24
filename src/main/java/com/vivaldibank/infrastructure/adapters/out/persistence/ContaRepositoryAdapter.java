package com.vivaldibank.infrastructure.adapters.out.persistence;

import com.vivaldibank.domain.model.Conta;
import com.vivaldibank.domain.ports.out.ContaRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ContaRepositoryAdapter implements ContaRepositoryPort {

    private final SpringDataContaRepository springRepository;
    private final ContaMapper contaMapper;


    @Override
    public Conta salvar(Conta conta) {
        ContaEntity entity = contaMapper.toEntity(conta);

        ContaEntity salvo = springRepository.save(entity);

        return contaMapper.toDomain(salvo);
    }

    @Override
    public Optional<Conta> buscarPorId(String id) {
        Optional<ContaEntity> entityOpt = springRepository.findById(UUID.fromString(id));
        return entityOpt.map(contaMapper::toDomain);
    }

    @Override
    public Optional<Conta> buscarPorIdParaAlteracao(String id) {
        Optional<ContaEntity> entityOpt = springRepository.findByIdWithLock(UUID.fromString(id));

        return entityOpt.map(contaMapper::toDomain);
    }

    @Override
    public Optional<Conta> buscarPorNumero(String numero) {
        Optional<ContaEntity> entityOpt = springRepository.findByNumero(numero);

        return entityOpt.map(contaMapper::toDomain);
    }

    @Override
    public Optional<String> buscarUltimoNumeroConta() {
        return springRepository.findUltimoNumeroConta();
    }
}
