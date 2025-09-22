package com.luizabau.gestordeestoque.service;

import com.luizabau.gestordeestoque.domain.Produto;
import com.luizabau.gestordeestoque.dto.ProdutoResponseDTO;
import com.luizabau.gestordeestoque.dto.ProdutoSimpleDTO;
import com.luizabau.gestordeestoque.mapper.ProdutoMapper;
import com.luizabau.gestordeestoque.repository.ProdutoRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class MovimentacaoDeEstoqueService {
    private final ProdutoRepository produtoRepository;
    private final ProdutoMapper produtoMapper;

    public Produto buscarProdutoPorId(Integer id) throws Exception {
        return produtoRepository.findById(id)
                .orElseThrow(() -> new Exception("Produto não encontrado com ID: " + id));
    }

    @Transactional
    public ProdutoResponseDTO reporEstoque(Integer produtoId, int quantidade, String usuario) throws Exception {
        Produto produto = buscarProdutoPorId(produtoId);
        if (produto.getEstoque() == null) {
            throw new Exception("Estoque não encontrado para produto com ID: " + produtoId);
        }
        produto.getEstoque().entrar(quantidade, usuario, "Reposição de estoque");
        produto = produtoRepository.save(produto);

        return produtoMapper.toResponseDTO(produto);
    }

    @Transactional
    public ProdutoResponseDTO sairEstoque(Integer produtoId, int quantidade, String usuario) throws Exception {
        Produto produto = buscarProdutoPorId(produtoId);
        if (produto.getEstoque() == null) {
            throw new Exception("Estoque não encontrado para produto com ID: " + produtoId);
        }
        if (produto.getDataAtualizacao().isAfter(LocalDateTime.now())) {
            throw new Exception("Não é permitido registrar movimentações com data futura");
        }
        produto.getEstoque().sair(quantidade, usuario, "Saída de estoque");
        produto = produtoRepository.save(produto);

        return produtoMapper.toResponseDTO(produto);
    }

    // RF11
    @Transactional(readOnly = true)
    public List<ProdutoSimpleDTO> listarProdutosPrecisandoReposicao() {
        List<Produto> produtos = produtoRepository.findAll();
        return produtos.stream()
                .filter(Produto::isPrecisoReposicao)
                .map(produtoMapper::toSimpleDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public boolean verificarDisponibilidade(Integer produtoId, int quantidadeDesejada) throws Exception {
        Produto produto = buscarProdutoPorId(produtoId);
        return produto.getEstoque() != null &&
                produto.getEstoque().getQuantidade() >= quantidadeDesejada;
    }

    @Transactional(readOnly = true)
    public ProdutoResponseDTO buscarProdutoDTO(Integer id) throws Exception {
        Produto produto = buscarProdutoPorId(id);
        return produtoMapper.toResponseDTO(produto);
    }
}