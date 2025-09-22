package com.luizabau.gestordeestoque.mapper;

import com.luizabau.gestordeestoque.domain.Categoria;
import com.luizabau.gestordeestoque.domain.Estoque;
import com.luizabau.gestordeestoque.domain.Fornecedor;
import com.luizabau.gestordeestoque.domain.Produto;
import com.luizabau.gestordeestoque.dto.*;
import org.springframework.stereotype.Component;

@Component
public class ProdutoMapper {

    public ProdutoResponseDTO toResponseDTO(Produto produto) {
        if (produto == null) return null;

        return ProdutoResponseDTO.builder()
                .id(produto.getId())
                .codigo(produto.getCodigo())
                .nome(produto.getNome())
                .descricao(produto.getDescricao())
                .preco(produto.getPreco())
                .unidadeMedida(produto.getUnidadeMedida())
                .dimensoes(produto.getDimensoes())
                .cor(produto.getCor())
                .quantidadeMinima(produto.getQuantidadeMinima())
                .quantidadeIdeal(produto.getQuantidadeIdeal())
                .quantidadeMaxima(produto.getQuantidadeMaxima())
                .ativo(produto.getAtivo())
                .categoria(toCategoriaSimple(produto.getCategoria()))
                .fornecedor(toFornecedorSimple(produto.getFornecedor()))
                .estoque(toEstoqueDTO(produto.getEstoque()))
                .dataCriacao(produto.getDataCriacao())
                .dataAtualizacao(produto.getDataAtualizacao())
                .usuarioCriacao(produto.getUsuarioCriacao())
                .usuarioAtualizacao(produto.getUsuarioAtualizacao())
                .precisaReposicao(produto.isPrecisoReposicao())
                .nivelEstoque(produto.getEstoque() != null ? produto.getEstoque().getNivelEstoque() : "SEM_ESTOQUE")
                .build();
    }

    public ProdutoSimpleDTO toSimpleDTO(Produto produto) {
        if (produto == null) return null;

        return ProdutoSimpleDTO.builder()
                .id(produto.getId())
                .codigo(produto.getCodigo())
                .nome(produto.getNome())
                .preco(produto.getPreco())
                .unidadeMedida(produto.getUnidadeMedida())
                .ativo(produto.getAtivo())
                .categoriaNome(produto.getCategoria() != null ? produto.getCategoria().getNome() : null)
                .fornecedorNome(produto.getFornecedor() != null ? produto.getFornecedor().getNome() : null)
                .quantidadeEstoque(produto.getEstoque() != null ? produto.getEstoque().getQuantidade() : 0)
                .nivelEstoque(produto.getEstoque() != null ? produto.getEstoque().getNivelEstoque() : "SEM_ESTOQUE")
                .precisaReposicao(produto.isPrecisoReposicao())
                .build();
    }

    public Produto toEntity(ProdutoCreateDTO createDTO, Categoria categoria, Fornecedor fornecedor) {
        if (createDTO == null) return null;

        return Produto.builder()
                .codigo(createDTO.getCodigo())
                .nome(createDTO.getNome())
                .descricao(createDTO.getDescricao())
                .preco(createDTO.getPreco())
                .unidadeMedida(createDTO.getUnidadeMedida())
                .dimensoes(createDTO.getDimensoes())
                .cor(createDTO.getCor())
                .quantidadeMinima(createDTO.getQuantidadeMinima())
                .quantidadeIdeal(createDTO.getQuantidadeIdeal())
                .quantidadeMaxima(createDTO.getQuantidadeMaxima())
                .ativo(createDTO.getAtivo())
                .categoria(categoria)
                .fornecedor(fornecedor)
                .build();
    }

    private FornecedorSimpleDTO toFornecedorSimple(Fornecedor fornecedor) {
        if (fornecedor == null) return null;

        return new FornecedorSimpleDTO(
                fornecedor.getId(),
                fornecedor.getNome(),
                fornecedor.getCnpj(),
                fornecedor.getEmail(),
                fornecedor.getAtivo()
        );
    }

    private CategoriaSimpleDTO toCategoriaSimple(Categoria categoria) {
        if (categoria == null) return null;
        return new CategoriaSimpleDTO(
                categoria.getId(),
                categoria.getNome(),
                categoria.getDescricao()
        );
    }

    private EstoqueSimpleDTO toEstoqueDTO(Estoque estoque) {
        if (estoque == null) return null;

        return EstoqueSimpleDTO.builder()
                .quantidade(estoque.getQuantidade())
                .quantidadeMinima(estoque.getQuantidadeMinima())
                .quantidadeMaxima(estoque.getQuantidadeMaxima())
                .situacao(estoque.getSituacao() != null ? estoque.getSituacao().name() : "NORMAL")
                .dataUltimaMovimentacao(estoque.getDataUltimaMovimentacao())
                .usuarioUltimaMovimentacao(estoque.getUsuarioUltimaMovimentacao())
                .nivelEstoque(estoque.getNivelEstoque())
                .estoqueBaixo(estoque.isEstoqueBaixo())
                .build();
    }
}