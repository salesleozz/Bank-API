package com.bank.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CartaoDTO {
    private Long id;
    private Long usuarioId;
    private String numeroCartao;
    private String titular;
    private String dataValidade;
    private String cvv;
    private BigDecimal limiteCredito;
    private BigDecimal saldoDisponivel;
    private BigDecimal faturaAtual;
    private String statusCartao;
    private String tipoCartao;
    private LocalDateTime dataCriacao;
}


