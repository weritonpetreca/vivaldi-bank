package com.vivaldibank.infrastructure.adapters.out.persistence;

import com.vivaldibank.domain.model.TipoMovimentacao;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tb_movimentacao")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MovimentacaoEntity {

    @Id
    private UUID id;

    private LocalDateTime dataHora;
    private BigDecimal valor;

    @Enumerated(EnumType.STRING)
    private TipoMovimentacao tipo;

    private String contraparteNome;
    private String contraparteNumeroConta;

    @ManyToOne
    @JoinColumn(name = "conta_id")
    private ContaEntity conta;

}

