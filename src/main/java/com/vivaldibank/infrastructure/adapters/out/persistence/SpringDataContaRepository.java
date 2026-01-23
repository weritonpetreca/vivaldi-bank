package com.vivaldibank.infrastructure.adapters.out.persistence;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface SpringDataContaRepository extends JpaRepository<ContaEntity, UUID> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT c FROM ContaEntity c WHERE c.id = :id")
    Optional<ContaEntity> findByIdWithLock(@Param("id") UUID id);

    Optional<ContaEntity> findByNumero(String numero);

    @Query(value = "SELECT c.numero FROM ContaEntity c ORDER BY c.numero DESC LIMIT 1")
    Optional<String> findUltimoNumeroConta();

    Optional<ContaEntity> findByCpf(String cpf);
}
