package com.vivaldibank.infrastructure.adapters.out.persistence;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "tb_contas")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ContaEntity {

    @Id
    private UUID id;

    @Column(name = "numero_conta", unique = true, nullable = false)
    private String numero;

    @Column(nullable = false)
    private BigDecimal saldo;

    @Column(name = "titular_nome", nullable = false)
    private String titularNome;

    @Column(unique = true, nullable = false)
    private String cpf;

    @Column(name = "criado_em", nullable = false)
    private LocalDateTime criadoEm;

    @OneToMany(mappedBy = "conta", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<MovimentacaoEntity> movimentacoes = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ContaEntity that = (ContaEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
