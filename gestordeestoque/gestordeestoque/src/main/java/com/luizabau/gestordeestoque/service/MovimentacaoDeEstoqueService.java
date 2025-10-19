package com.luizabau.gestordeestoque.service;

import com.luizabau.gestordeestoque.domain.Estoque;
import com.luizabau.gestordeestoque.domain.MovimentacaoEstoque;
import com.luizabau.gestordeestoque.domain.Produto;
import com.luizabau.gestordeestoque.dto.CategoriaResponseDTO;
import com.luizabau.gestordeestoque.dto.MovimentacaoEstoqueResponseDTO;
import com.luizabau.gestordeestoque.dto.ProdutoResponseDTO;
import com.luizabau.gestordeestoque.repository.EstoqueRepository;
import com.luizabau.gestordeestoque.repository.MovimentacaoEstoqueRepository;
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
    private final EstoqueRepository estoqueRepository;
    private final MovimentacaoEstoqueRepository movimentacaoEstoqueRepository;

    public Produto buscarProdutoPorId(Integer id) throws Exception {
        return produtoRepository.findById(id)
                .orElseThrow(() -> new Exception("Produto não encontrado com ID: " + id));
    }

    @Transactional
    public ProdutoResponseDTO reporEstoque(Integer produtoId, int quantidade, String usuario) throws Exception {
        Produto produto = buscarProdutoPorId(produtoId);

        if (produto.getEstoque() == null) {
            Estoque estoque = Estoque.builder()
                    .produto(produto)
                    .quantidade(0)
                    .quantidadeMinima(produto.getQuantidadeMinima())
                    .quantidadeIdeal(produto.getQuantidadeIdeal())
                    .quantidadeMaxima(produto.getQuantidadeMaxima())
                    .build();

            estoque = estoqueRepository.save(estoque);
            produto.setEstoque(estoque);

            System.out.println(" [" + LocalDateTime.now() + "] Estoque criado para produto ID: " + produtoId + " por " + usuario);
        }

        MovimentacaoEstoque movimentacao = produto.getEstoque().entrar(quantidade, usuario, "Reposição de estoque");

        produto = produtoRepository.save(produto);
        movimentacaoEstoqueRepository.save(movimentacao);

        System.out.println(" [" + LocalDateTime.now() + "] Reposição: Produto=" + produtoId + ", Qtd=" + quantidade + ", Total=" + produto.getEstoque().getQuantidade() + " por " + usuario);

        return ProdutoResponseDTO.from(produto);
    }

    @Transactional
    public ProdutoResponseDTO sairEstoque(Integer produtoId, int quantidade, String usuario) throws Exception {
        Produto produto = buscarProdutoPorId(produtoId);

        if (produto.getEstoque() == null) {
            throw new Exception("Não há estoque cadastrado para este produto. Realize uma reposição primeiro.");
        }

        if (produto.getDataAtualizacao() != null && produto.getDataAtualizacao().isAfter(LocalDateTime.now())) {
            throw new Exception("Não é permitido registrar movimentações com data futura");
        }

        MovimentacaoEstoque movimentacao = produto.getEstoque().sair(quantidade, usuario, "Saída de estoque");
        produto = produtoRepository.save(produto);
        movimentacaoEstoqueRepository.save(movimentacao);

        System.out.println(" [" + LocalDateTime.now() + "] Saída: Produto=" + produtoId + ", Qtd=" + quantidade + ", Restante=" + produto.getEstoque().getQuantidade() + " por " + usuario);

        return ProdutoResponseDTO.from(produto);
    }


    @Transactional(readOnly = true)
    public List<MovimentacaoEstoqueResponseDTO> buscarHistorico(Integer produtoId) throws Exception {
        buscarProdutoPorId(produtoId);
        List<MovimentacaoEstoque> movimentacoes = movimentacaoEstoqueRepository.findHistoricoPorProduto(produtoId);

        return movimentacoes.stream()
                .map(MovimentacaoEstoqueResponseDTO::from)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<Produto> listarProdutosPrecisandoReposicao() {
        return produtoRepository.findProdutosComEstoqueBaixo();
    }

}