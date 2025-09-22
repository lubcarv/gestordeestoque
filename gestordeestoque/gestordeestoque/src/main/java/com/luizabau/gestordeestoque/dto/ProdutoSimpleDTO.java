package com.luizabau.gestordeestoque.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProdutoSimpleDTO {
    private Integer id;
    private String codigo;
    private String nome;
    private BigDecimal preco;
    private String unidadeMedida;
    private Boolean ativo;
    private String categoriaNome;
    private String fornecedorNome;
    private Integer quantidadeEstoque;
    private String nivelEstoque;
    private Boolean precisaReposicao;
}