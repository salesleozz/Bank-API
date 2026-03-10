package com.bank.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UsuarioCriacaoDTO {
    private String nome;
    private String email;
    private String senha;
    private String cpf;
    private String telefone;
    private String dataNascimento;
    private String endereco;
}

