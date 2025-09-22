package com.luizabau.gestordeestoque.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoriaUpdateDTO {

    @Size(min = 2, max = 50, message = "Nome deve ter entre 2 e 50 caracteres")
    private String nome;

    @Size(max = 200, message = "Descrição deve ter no máximo 200 caracteres")
    private String descricao;

    private Boolean ativa;

}