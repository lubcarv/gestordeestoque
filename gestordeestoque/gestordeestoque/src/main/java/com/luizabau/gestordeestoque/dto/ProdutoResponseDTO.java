package com.luizabau.gestordeestoque.dto;

import com.luizabau.gestordeestoque.domain.Produto;
import com.luizabau.gestordeestoque.domain.SituacaoEstoque;
import lombok.*;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProdutoResponseDTO {
    private Integer id;
    private String codigo;
    private String nome;
    private String descricao;
    private BigDecimal preco;
    private String unidadeMedida;
    private String dimensoes;
    private String cor;
    private Integer quantidadeEstoque;
    private Integer quantidadeMinima;
    private Integer quantidadeIdeal;
    private Integer quantidadeMaxima;
    private Boolean ativo;

    private Integer categoriaId;
    private String categoriaNome;

    private Integer fornecedorId;
    private String fornecedorNome;

    private SituacaoEstoque situacaoEstoque;


    public static ProdutoResponseDTO from(Produto produto) {
        ProdutoResponseDTO dto = new ProdutoResponseDTO();
        dto.id = produto.getId();
        dto.codigo = produto.getCodigo();
        dto.nome = produto.getNome();
        dto.descricao = produto.getDescricao();
        dto.preco = produto.getPreco();
        dto.unidadeMedida = produto.getUnidadeMedida();
        dto.dimensoes = produto.getDimensoes();
        dto.cor = produto.getCor();
        dto.quantidadeEstoque = produto.getEstoque() != null ? produto.getEstoque().getQuantidade() : 0;
        dto.quantidadeMinima = produto.getQuantidadeMinima();
        dto.quantidadeIdeal = produto.getQuantidadeIdeal();
        dto.quantidadeMaxima = produto.getQuantidadeMaxima();
        dto.ativo = produto.getAtivo();

        dto.categoriaId = produto.getCategoria() != null ? produto.getCategoria().getId() : null;
        dto.categoriaNome = produto.getCategoria() != null ? produto.getCategoria().getNome() : null;

        dto.fornecedorId = produto.getFornecedor() != null ? produto.getFornecedor().getId() : null;
        dto.fornecedorNome = produto.getFornecedor() != null ? produto.getFornecedor().getNome() : null;

        dto.situacaoEstoque = produto.getEstoque() != null
                ? produto.getEstoque().getSituacao()
                : SituacaoEstoque.SEM_ESTOQUE;
        return dto;
    }
}