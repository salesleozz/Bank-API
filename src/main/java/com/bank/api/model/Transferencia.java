package com.bank.api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transferencias")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transferencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_origem_id", nullable = false)
    private Usuario usuarioOrigem;

    @ManyToOne
    @JoinColumn(name = "usuario_destino_id", nullable = false)
    private Usuario usuarioDestino;

    @NotNull(message = "Valor é obrigatório")
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal valor;

    @Column(length = 500)
    private String descricao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoTransferencia tipoTransferencia;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusTransferencia statusTransferencia;

    @Column(nullable = false)
    private LocalDateTime dataTransferencia;

    @Column(nullable = false)
    private LocalDateTime dataCriacao;

    @PrePersist
    public void prePersist() {
        dataCriacao = LocalDateTime.now();
        if (dataTransferencia == null) {
            dataTransferencia = LocalDateTime.now();
        }
    }
}


