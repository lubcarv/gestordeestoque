package com.luizabau.gestordeestoque.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoriaVendasDTO {
    private Integer categoriaId;
    private String nomeCategoria;
    private Integer quantidade;
    private Double percentual;
}