package com.luizabau.gestordeestoque.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoriaResponseDTO {
    private Integer id;
    private String nome;
    private String descricao;
    private Boolean ativa;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;
    private Integer totalProdutos;
}