package com.bank.api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "emprestimos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Emprestimo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @NotNull(message = "Valor é obrigatório")
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal valorTotal;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal saldoDevedor;

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal taxaJuros;

    @NotNull(message = "Número de parcelas é obrigatório")
    @Column(nullable = false)
    private Integer numeroParcelas;

    @Column(nullable = false)
    private Integer parcelasQuitadas;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusEmprestimo statusEmprestimo;

    @Column(nullable = false)
    private LocalDateTime dataContratacao;

    @Column(nullable = false)
    private LocalDateTime dataPrimeiroVencimento;

    @Column(nullable = false)
    private LocalDateTime dataProximoVencimento;

    @Column(nullable = false)
    private LocalDateTime dataCriacao;

    @Column(nullable = false)
    private LocalDateTime dataAtualizacao;

    @PrePersist
    public void prePersist() {
        dataCriacao = LocalDateTime.now();
        dataAtualizacao = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        dataAtualizacao = LocalDateTime.now();
    }
}


