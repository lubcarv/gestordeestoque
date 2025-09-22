package com.luizabau.gestordeestoque.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProdutoUpdateDTO {

    @Size(max = 50, message = "Código deve ter no máximo 50 caracteres")
    private String codigo;

    @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
    private String nome;

    @Size(max = 500, message = "Descrição deve ter no máximo 500 caracteres")
    private String descricao;

    @Positive(message = "Preço deve ser positivo")
    private BigDecimal preco;

    @Size(max = 10, message = "Unidade de medida deve ter no máximo 10 caracteres")
    private String unidadeMedida;

    @Size(max = 100, message = "Dimensões deve ter no máximo 100 caracteres")
    private String dimensoes;

    @Size(max = 30, message = "Cor deve ter no máximo 30 caracteres")
    private String cor;

    @PositiveOrZero(message = "Quantidade mínima deve ser zero ou positiva")
    private Integer quantidadeMinima;

    @PositiveOrZero(message = "Quantidade ideal deve ser zero ou positiva")
    private Integer quantidadeIdeal;

    @PositiveOrZero(message = "Quantidade máxima deve ser zero ou positiva")
    private Integer quantidadeMaxima;

    private Boolean ativo;

    private Integer categoriaId;
    private Integer fornecedorId;
}