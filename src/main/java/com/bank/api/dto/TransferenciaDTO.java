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
public class TransferenciaDTO {
    private Long id;
    private Long usuarioOrigemId;
    private Long usuarioDestinoId;
    private String usuarioOrigemNome;
    private String usuarioDestinoNome;
    private BigDecimal valor;
    private String descricao;
    private String tipoTransferencia;
    private String statusTransferencia;
    private LocalDateTime dataTransferencia;
    private LocalDateTime dataCriacao;
}


