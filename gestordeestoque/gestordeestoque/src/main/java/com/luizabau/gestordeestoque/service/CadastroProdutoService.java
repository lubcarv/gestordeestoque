package com.luizabau.gestordeestoque.service;

import com.luizabau.gestordeestoque.domain.Categoria;
import com.luizabau.gestordeestoque.domain.Fornecedor;
import com.luizabau.gestordeestoque.domain.Produto;
import com.luizabau.gestordeestoque.dto.ProdutoCreateDTO;
import com.luizabau.gestordeestoque.dto.ProdutoResponseDTO;
import com.luizabau.gestordeestoque.dto.ProdutoSimpleDTO;
import com.luizabau.gestordeestoque.dto.ProdutoUpdateDTO;
import com.luizabau.gestordeestoque.mapper.ProdutoMapper;
import com.luizabau.gestordeestoque.repository.CategoriaRepository;
import com.luizabau.gestordeestoque.repository.FornecedorRepository;
import com.luizabau.gestordeestoque.repository.ProdutoRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class CadastroProdutoService {
    private final ProdutoRepository produtoRepository;
    private final FornecedorRepository fornecedorRepository;
    private final CategoriaRepository categoriaRepository;
    private final ProdutoMapper produtoMapper;

    @Transactional
    public ProdutoResponseDTO criarProduto(ProdutoCreateDTO createDTO) throws Exception {
        Categoria categoria = categoriaRepository.findById(createDTO.getCategoriaId())
                .orElseThrow(() -> new Exception("Categoria não encontrada"));

        Fornecedor fornecedor = null;
        if (createDTO.getFornecedorId() != null) {
            fornecedor = fornecedorRepository.findById(createDTO.getFornecedorId())
                    .orElseThrow(() -> new Exception("Fornecedor não encontrado"));
        }

        if (produtoRepository.existsByCodigo(createDTO.getCodigo())) {
            throw new Exception("Já existe um produto com o código: " + createDTO.getCodigo());
        }

        Produto produto = Produto.builder()
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

        produto = produtoRepository.save(produto);
        return produtoMapper.toResponseDTO(produto);
    }

    @Transactional
    public ProdutoResponseDTO atualizarProduto(Integer id, ProdutoUpdateDTO updateDTO) throws Exception {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new Exception("Produto não encontrado"));

        if (updateDTO.getCodigo() != null) {
            if (!produto.getCodigo().equals(updateDTO.getCodigo()) &&
                    produtoRepository.existsByCodigo(updateDTO.getCodigo())) {
                throw new Exception("Já existe um produto com o código: " + updateDTO.getCodigo());
            }
            produto.setCodigo(updateDTO.getCodigo());
        }

        if (updateDTO.getNome() != null) produto.setNome(updateDTO.getNome());
        if (updateDTO.getDescricao() != null) produto.setDescricao(updateDTO.getDescricao());
        if (updateDTO.getPreco() != null) produto.setPreco(updateDTO.getPreco());
        if (updateDTO.getUnidadeMedida() != null) produto.setUnidadeMedida(updateDTO.getUnidadeMedida());
        if (updateDTO.getDimensoes() != null) produto.setDimensoes(updateDTO.getDimensoes());
        if (updateDTO.getCor() != null) produto.setCor(updateDTO.getCor());
        if (updateDTO.getQuantidadeMinima() != null) produto.setQuantidadeMinima(updateDTO.getQuantidadeMinima());
        if (updateDTO.getQuantidadeIdeal() != null) produto.setQuantidadeIdeal(updateDTO.getQuantidadeIdeal());
        if (updateDTO.getQuantidadeMaxima() != null) produto.setQuantidadeMaxima(updateDTO.getQuantidadeMaxima());
        if (updateDTO.getAtivo() != null) produto.setAtivo(updateDTO.getAtivo());

        if (updateDTO.getCategoriaId() != null) {
            Categoria categoria = categoriaRepository.findById(updateDTO.getCategoriaId())
                    .orElseThrow(() -> new Exception("Categoria não encontrada"));
            produto.setCategoria(categoria);
        }

        if (updateDTO.getFornecedorId() != null) {
            Fornecedor fornecedor = fornecedorRepository.findById(updateDTO.getFornecedorId())
                    .orElseThrow(() -> new Exception("Fornecedor não encontrado"));
            produto.setFornecedor(fornecedor);
        }

        produto = produtoRepository.save(produto);
        return produtoMapper.toResponseDTO(produto);
    }

    @Transactional(readOnly = true)
    public List<ProdutoSimpleDTO> listarProdutos() {
        return produtoRepository.findAll().stream()
                .map(produtoMapper::toSimpleDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ProdutoResponseDTO buscarProduto(Integer id) throws Exception {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new Exception("Produto não encontrado"));
        return produtoMapper.toResponseDTO(produto);
    }

    @Transactional
    public Produto salvar(Produto produto) {
        produto = produtoRepository.save(produto);
        if (produto.getCategoria() != null && produto.getCategoria().getId() != null) {
            Categoria categoria = categoriaRepository.findById(produto.getCategoria().getId()).orElse(null);
            produto.setCategoria(categoria);
        }
        if (produto.getFornecedor() != null && produto.getFornecedor().getId() != null) {
            Fornecedor fornecedor = fornecedorRepository.findById(produto.getFornecedor().getId()).orElse(null);
            produto.setFornecedor(fornecedor);
        }
        return produto;
    }

    @Transactional
    public void excluir(Integer id) {
        produtoRepository.deleteById(id);
    }
}