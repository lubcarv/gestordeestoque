package com.luizabau.gestordeestoque.service;

import com.luizabau.gestordeestoque.domain.Categoria;
import com.luizabau.gestordeestoque.domain.Fornecedor;
import com.luizabau.gestordeestoque.domain.Produto;
import com.luizabau.gestordeestoque.domain.SituacaoEstoque;
import com.luizabau.gestordeestoque.dto.ProdutoCreateDTO;
import com.luizabau.gestordeestoque.dto.ProdutoResponseDTO;
import com.luizabau.gestordeestoque.dto.ProdutoUpdateDTO;
import com.luizabau.gestordeestoque.repository.CategoriaRepository;
import com.luizabau.gestordeestoque.repository.FornecedorRepository;
import com.luizabau.gestordeestoque.repository.ProdutoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class CadastroProdutoService {
    private final ProdutoRepository produtoRepository;
    private final FornecedorRepository fornecedorRepository;
    private final CategoriaRepository categoriaRepository;

    @Transactional
    public ProdutoResponseDTO criarProduto(ProdutoCreateDTO createDTO) throws Exception {
        Categoria categoria = categoriaRepository.findById(createDTO.getCategoriaId())
                .orElseThrow(() -> new Exception("Categoria não encontrada"));
        Fornecedor fornecedor = fornecedorRepository.findById(createDTO.getFornecedorId())
                .orElseThrow(() -> new Exception("Fornecedor não encontrado"));

        if (produtoRepository.existsByCodigo(createDTO.getCodigo())) {
            throw new Exception("Já existe um produto com o código: " + createDTO.getCodigo());
        }
        if (produtoRepository.existeNomeECategoria(createDTO.getNome(), createDTO.getCategoriaId())) {
            throw new Exception("Já existe um produto com este nome para a mesma categoria.");
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

        return ProdutoResponseDTO.from(produto);
    }

    @Transactional
    public ProdutoResponseDTO atualizarProduto(Integer id, ProdutoUpdateDTO updateDTO) throws Exception {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new Exception("Produto não encontrado"));

        String novoNome = updateDTO.getNome() != null ? updateDTO.getNome() : produto.getNome();
        Integer novaCategoriaId = updateDTO.getCategoriaId() != null ? updateDTO.getCategoriaId() : produto.getCategoria().getId();

        if (produtoRepository.existeNomeECategoria(novoNome, novaCategoriaId)
                && !(novoNome.equalsIgnoreCase(produto.getNome()) && novaCategoriaId.equals(produto.getCategoria().getId()))) {
            throw new Exception("Já existe um produto com este nome para a mesma categoria.");
        }

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

        if (produto.getEstoque() != null) {
        if (produto.getEstoque().getSituacao() == null) {
            produto.getEstoque().setSituacao(SituacaoEstoque.SEM_ESTOQUE);
        }
        produto.getEstoque().atualizarSituacao();
    }
        produto = produtoRepository.save(produto);

        return ProdutoResponseDTO.from(produto);
    }


    @Transactional(readOnly = true)
    public List<ProdutoResponseDTO> listarProdutos() {
        return produtoRepository.findAll().stream()
                .map(ProdutoResponseDTO::from)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ProdutoResponseDTO buscarProduto(Integer id) throws Exception {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new Exception("Produto não encontrado"));
        return ProdutoResponseDTO.from(produto);
    }

    @Transactional
    public ProdutoResponseDTO ativarProduto(Integer id) throws Exception {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new Exception("Produto não encontrado com ID: " + id));

        if (produto.getAtivo()) {
            throw new Exception("Produto já está ativo");
        }

        produto.ativar();
        produto = produtoRepository.save(produto);

        return ProdutoResponseDTO.from(produto);
    }

    @Transactional
    public ProdutoResponseDTO inativarProduto(Integer id) throws Exception {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new Exception("Produto não encontrado com ID: " + id));

        if (!produto.getAtivo()) {
            throw new Exception("Produto já está inativo");
        }

        produto.inativar();
        produto = produtoRepository.save(produto);

        return ProdutoResponseDTO.from(produto);
    }

    @Transactional
    public void excluir(Integer id) throws Exception {
        if (!produtoRepository.existsById(id)) {
            throw new Exception("Produto não encontrado");
        }
        produtoRepository.deleteById(id);

    }

    @Transactional(readOnly = true)
    public List<ProdutoResponseDTO> buscarComFiltros(
            String codigo,
            String nome,
            Integer categoriaId,
            Integer fornecedorId,
            Boolean ativo
    ) {
        List<Produto> produtos = produtoRepository.findByFiltros(
                codigo,
                nome,
                categoriaId,
                fornecedorId,
                ativo
        );

        return produtos.stream()
                .map(ProdutoResponseDTO::from)
                .collect(Collectors.toList());
    }
}

