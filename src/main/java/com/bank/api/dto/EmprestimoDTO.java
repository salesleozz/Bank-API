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
public class EmprestimoDTO {
    private Long id;
    private Long usuarioId;
    private BigDecimal valorTotal;
    private BigDecimal saldoDevedor;
    private BigDecimal taxaJuros;
    private Integer numeroParcelas;
    private Integer parcelasQuitadas;
    private String statusEmprestimo;
    private LocalDateTime dataContratacao;
    private LocalDateTime dataPrimeiroVencimento;
    private LocalDateTime dataProximoVencimento;
}


