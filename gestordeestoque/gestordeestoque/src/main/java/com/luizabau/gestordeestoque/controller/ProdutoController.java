package com.luizabau.gestordeestoque.controller;

import com.luizabau.gestordeestoque.dto.*;
import com.luizabau.gestordeestoque.service.CadastroProdutoService;
import com.luizabau.gestordeestoque.service.MovimentacaoDeEstoqueService;
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
    public ResponseEntity<List<ProdutoSimpleDTO>> listar() {
        List<ProdutoSimpleDTO> produtos = cadastroProdutoService.listarProdutos();
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
    public ResponseEntity<List<ProdutoSimpleDTO>> listarEstoqueBaixo() {
        List<ProdutoSimpleDTO> produtos = movimentacaoService.listarProdutosPrecisandoReposicao();
        return ResponseEntity.ok(produtos);
    }

    @GetMapping("/{id}/disponibilidade")
    public ResponseEntity<Boolean> verificarDisponibilidade(
            @PathVariable Integer id,
            @RequestParam int quantidade) {
        try {
            boolean disponivel = movimentacaoService.verificarDisponibilidade(id, quantidade);
            return ResponseEntity.ok(disponivel);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<ProdutoResponseDTO> criar(@Valid @RequestBody ProdutoCreateDTO createDTO) {
        try {
            ProdutoResponseDTO produto = cadastroProdutoService.criarProduto(createDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(produto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
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
            return ResponseEntity.notFound().build();
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

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Integer id) {
        try {
            cadastroProdutoService.excluir(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}