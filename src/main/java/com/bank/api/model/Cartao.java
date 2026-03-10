package com.bank.api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "cartoes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cartao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @NotBlank(message = "Número do cartão é obrigatório")
    @Column(nullable = false, unique = true)
    private String numeroCartao;

    @NotBlank(message = "Titular é obrigatório")
    @Column(nullable = false)
    private String titular;

    @NotBlank(message = "Data de validade é obrigatória")
    @Column(nullable = false)
    private String dataValidade;

    @NotBlank(message = "CVV é obrigatório")
    @Column(nullable = false)
    private String cvv;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal limiteCredito;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal saldoDisponivel;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal faturaAtual;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusCartao statusCartao;

    @NotNull(message = "Tipo de cartão é obrigatório")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoCartao tipoCartao;

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


