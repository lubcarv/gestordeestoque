package com.luizabau.gestordeestoque.dto;

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
public class ProdutoResponseDTO {
    private Integer id;
    private String codigo;
    private String nome;
    private String descricao;
    private BigDecimal preco;
    private String unidadeMedida;
    private String dimensoes;
    private String cor;
    private Integer quantidadeMinima;
    private Integer quantidadeIdeal;
    private Integer quantidadeMaxima;
    private Boolean ativo;

    private CategoriaSimpleDTO categoria;
    private FornecedorSimpleDTO fornecedor;
    private EstoqueSimpleDTO estoque;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;
    private String usuarioCriacao;
    private String usuarioAtualizacao;

    private Boolean precisaReposicao;
    private String nivelEstoque;
}