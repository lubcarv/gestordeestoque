package com.luizabau.gestordeestoque.controller;

import com.luizabau.gestordeestoque.dto.*;
import com.luizabau.gestordeestoque.service.FornecedorService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fornecedores")
@AllArgsConstructor
@Tag(name = "Fornecedores")
public class FornecedorController {

    private final FornecedorService fornecedorService;

    @GetMapping
    public ResponseEntity<List<FornecedorResponseDTO>> listar() {
        List<FornecedorResponseDTO> fornecedores = fornecedorService.listarTodos();
        return ResponseEntity.ok(fornecedores);
    }

    @GetMapping("/ativos")
    public ResponseEntity<List<FornecedorSimpleDTO>> listarAtivos() {
        List<FornecedorSimpleDTO> fornecedores = fornecedorService.listarAtivos();
        return ResponseEntity.ok(fornecedores);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FornecedorResponseDTO> buscar(@PathVariable Integer id) {
        try {
            FornecedorResponseDTO fornecedor = fornecedorService.buscarPorId(id);
            return ResponseEntity.ok(fornecedor);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/nome/{nome}")
    public ResponseEntity<List<FornecedorResponseDTO>> buscarPorNome(@PathVariable String nome) {
        try {
            List<FornecedorResponseDTO> fornecedores = fornecedorService.buscarPorNome(nome);
            return ResponseEntity.ok(fornecedores);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<FornecedorResponseDTO> criar(@Valid @RequestBody FornecedorCreateDTO createDTO) {
        try {
            FornecedorResponseDTO fornecedor = fornecedorService.criar(createDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(fornecedor);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .header("Error-Message", e.getMessage())
                    .build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<FornecedorResponseDTO> atualizar(
            @PathVariable Integer id,
            @Valid @RequestBody FornecedorUpdateDTO updateDTO) {
        try {
            FornecedorResponseDTO fornecedor = fornecedorService.atualizar(id, updateDTO);
            return ResponseEntity.ok(fornecedor);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .header("Error-Message", e.getMessage())
                    .build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Integer id) {
        try {
            fornecedorService.excluir(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .header("Error-Message", e.getMessage())
                    .build();
        }
    }
}