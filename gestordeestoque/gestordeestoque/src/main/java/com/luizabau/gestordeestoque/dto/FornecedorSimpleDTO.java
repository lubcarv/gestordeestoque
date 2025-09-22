package com.luizabau.gestordeestoque.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FornecedorSimpleDTO {
    private Integer id;
    private String nome;
    private String cnpj;
    private String email;
    private Boolean ativo;
}