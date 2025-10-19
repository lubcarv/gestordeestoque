package com.luizabau.gestordeestoque.controller;

import com.luizabau.gestordeestoque.domain.MovimentacaoEstoque;
import com.luizabau.gestordeestoque.dto.*;
import com.luizabau.gestordeestoque.service.CadastroProdutoService;
import com.luizabau.gestordeestoque.service.MovimentacaoDeEstoqueService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/produtos")
@AllArgsConstructor
@Tag(name = "Produtos")
public class ProdutoController {

    private final CadastroProdutoService cadastroProdutoService;
    private final MovimentacaoDeEstoqueService movimentacaoService;


    @GetMapping
    public ResponseEntity<List<ProdutoResponseDTO>> listar() {
        List<ProdutoResponseDTO> produtos = cadastroProdutoService.listarProdutos();
        return ResponseEntity.ok(produtos);
    }

    @GetMapping("/filtrar")
    @Operation(
            summary = "Filtrar produtos",
            description = "Busca produtos usando múltiplos filtros opcionais. " +
                    "Todos os parâmetros são opcionais e podem ser combinados."
    )
    public ResponseEntity<List<ProdutoResponseDTO>> filtrar(
            @Parameter(description = "Código do produto (busca parcial, ignora maiúsculas/minúsculas)")
            @RequestParam(required = false) String codigo,

            @Parameter(description = "Nome do produto (busca parcial, ignora maiúsculas/minúsculas)")
            @RequestParam(required = false) String nome,

            @Parameter(description = "ID da categoria")
            @RequestParam(required = false) Integer categoriaId,

            @Parameter(description = "ID do fornecedor")
            @RequestParam(required = false) Integer fornecedorId,

            @Parameter(description = "Status do produto (true=ativo, false=inativo)")
            @RequestParam(required = false) Boolean ativo
    ) {
        List<ProdutoResponseDTO> produtos = cadastroProdutoService.buscarComFiltros(
                codigo,
                nome,
                categoriaId,
                fornecedorId,
                ativo
        );

        return ResponseEntity.ok(produtos);
    }


    @GetMapping("/{id}")
    public ResponseEntity<ProdutoResponseDTO> buscar(@PathVariable Integer id) {
        try {
            ProdutoResponseDTO produto = cadastroProdutoService.buscarProduto(id);
            return ResponseEntity.ok(produto);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }


    @GetMapping("/estoque-baixo")
    public ResponseEntity<List<ProdutoResponseDTO>> listarEstoqueBaixo() {
        List<ProdutoResponseDTO> produtos = movimentacaoService.listarProdutosPrecisandoReposicao()
                .stream()
                .map(ProdutoResponseDTO::from)
                .toList();
        return ResponseEntity.ok(produtos);
    }


    @GetMapping("/{produtoId}/historico")
    public ResponseEntity<List<MovimentacaoEstoqueResponseDTO>> buscarHistorico(@PathVariable Integer produtoId) {
        try {
            List<MovimentacaoEstoqueResponseDTO> historico = movimentacaoService.buscarHistorico(produtoId);
            return ResponseEntity.ok(historico);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping
    public ResponseEntity<ProdutoResponseDTO> criar(@Valid @RequestBody ProdutoCreateDTO createDTO) {
        try {
            ProdutoResponseDTO produto = cadastroProdutoService.criarProduto(createDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(produto);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .header("Error-Message", e.getMessage())
                    .build();
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity<ProdutoResponseDTO> atualizar(
            @PathVariable Integer id,
            @Valid @RequestBody ProdutoUpdateDTO updateDTO) {
        try {
            ProdutoResponseDTO produto = cadastroProdutoService.atualizarProduto(id, updateDTO);
            return ResponseEntity.ok(produto);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .header("Error-Message", e.getMessage())
                    .build();
        }
    }


    @PutMapping("/{id}/repor")
    public ResponseEntity<ProdutoResponseDTO> reporEstoque(
            @PathVariable Integer id,
            @RequestParam int quantidade,
            @RequestParam(defaultValue = "lubcarv") String usuario) {
        try {
            ProdutoResponseDTO produto = movimentacaoService.reporEstoque(id, quantidade, usuario);
            return ResponseEntity.ok(produto);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .header("Error-Message", e.getMessage())
                    .build();
        }
    }


    @PutMapping("/{id}/retirar")
    public ResponseEntity<ProdutoResponseDTO> retirarEstoque(
            @PathVariable Integer id,
            @RequestParam int quantidade,
            @RequestParam(defaultValue = "lubcarv") String usuario) {
        try {
            ProdutoResponseDTO produto = movimentacaoService.sairEstoque(id, quantidade, usuario);
            return ResponseEntity.ok(produto);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .header("Error-Message", e.getMessage())
                    .build();
        }
    }

    @PutMapping("/{id}/inativar")
    public ResponseEntity<ProdutoResponseDTO> inativarProduto(
            @PathVariable Integer id,
            @RequestParam(defaultValue = "lubcarv") String usuario) {
        try {
            ProdutoResponseDTO produto = cadastroProdutoService.inativarProduto(id);
            return ResponseEntity.ok(produto);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .header("Error-Message", e.getMessage())
                    .build();
        }
    }
    @PutMapping("/{id}/ativar")
    public ResponseEntity<ProdutoResponseDTO> ativarProduto(
            @PathVariable Integer id,
            @RequestParam(defaultValue = "lubcarv") String usuario) {
        try {
            ProdutoResponseDTO produto = cadastroProdutoService.ativarProduto(id);
            return ResponseEntity.ok(produto);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .header("Error-Message", e.getMessage())
                    .build();
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Integer id) {
        try {
            cadastroProdutoService.excluir(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .header("Error-Message", e.getMessage())
                    .build();
        }
    }
}