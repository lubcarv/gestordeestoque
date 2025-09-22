package com.luizabau.gestordeestoque.controller;

import com.luizabau.gestordeestoque.dto.*;
import com.luizabau.gestordeestoque.service.CategoriaService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categorias")
@AllArgsConstructor
@Tag(name = "Categorias")
public class CategoriaController {

    private final CategoriaService categoriaService;

    @GetMapping
    public ResponseEntity<List<CategoriaResponseDTO>> listar() {
        List<CategoriaResponseDTO> categorias = categoriaService.listarTodas();
        return ResponseEntity.ok(categorias);
    }

    @GetMapping("/ativas")
    public ResponseEntity<List<CategoriaSimpleDTO>> listarAtivas() {
        List<CategoriaSimpleDTO> categorias = categoriaService.listarAtivas();
        return ResponseEntity.ok(categorias);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoriaResponseDTO> buscar(@PathVariable Integer id) {
        try {
            CategoriaResponseDTO categoria = categoriaService.buscarPorId(id);
            return ResponseEntity.ok(categoria);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/descricao/{descricao}")
    public ResponseEntity<List<CategoriaResponseDTO>> buscarPorDescricao(@PathVariable String descricao) {
        try {
            List<CategoriaResponseDTO> categorias = categoriaService.buscarPorDescricao(descricao);
            return ResponseEntity.ok(categorias);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<CategoriaResponseDTO> criar(@Valid @RequestBody CategoriaCreateDTO createDTO) {
        try {
            CategoriaResponseDTO categoria = categoriaService.criar(createDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(categoria);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .header("Error-Message", e.getMessage())
                    .build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoriaResponseDTO> atualizar(
            @PathVariable Integer id,
            @Valid @RequestBody CategoriaUpdateDTO updateDTO) {
        try {
            CategoriaResponseDTO categoria = categoriaService.atualizar(id, updateDTO);
            return ResponseEntity.ok(categoria);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .header("Error-Message", e.getMessage())
                    .build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Integer id) {
        try {
            categoriaService.excluir(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .header("Error-Message", e.getMessage())
                    .build();
        }
    }
}