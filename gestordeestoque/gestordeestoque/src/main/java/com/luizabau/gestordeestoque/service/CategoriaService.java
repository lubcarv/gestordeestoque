package com.luizabau.gestordeestoque.service;

import com.luizabau.gestordeestoque.domain.Categoria;
import com.luizabau.gestordeestoque.dto.CategoriaCreateDTO;
import com.luizabau.gestordeestoque.dto.CategoriaResponseDTO;
import com.luizabau.gestordeestoque.dto.CategoriaUpdateDTO;
import com.luizabau.gestordeestoque.repository.CategoriaRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;


    @Transactional(readOnly = true)
    public List<CategoriaResponseDTO> listarTodas() {
        return categoriaRepository.findAll().stream()
                .map(CategoriaResponseDTO::from)
                .collect(Collectors.toList());
    }


    @Transactional(readOnly = true)
    public CategoriaResponseDTO buscarPorId(Integer id) throws Exception {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new Exception("Categoria não encontrada"));
        return CategoriaResponseDTO.from(categoria);
    }


    @Transactional(readOnly = true)
    public List<CategoriaResponseDTO> buscarPorDescricao(String descricao) throws Exception {
        List<Categoria> categorias = categoriaRepository.findByDescricaoContainingIgnoreCase(descricao);
        if (categorias.isEmpty()) {
            throw new Exception("Nenhuma categoria encontrada com a descrição: " + descricao);
        }
        return categorias.stream()
                .map(CategoriaResponseDTO::from)
                .collect(Collectors.toList());
    }


    @Transactional
    public CategoriaResponseDTO criar(CategoriaCreateDTO createDTO) throws Exception {
        if (categoriaRepository.existsByNomeIgnoreCase(createDTO.getNome())) {
            throw new Exception("Já existe uma categoria com este nome");
        }

        Categoria categoria = Categoria.builder()
                .nome(createDTO.getNome())
                .descricao(createDTO.getDescricao())
                .ativa(createDTO.getAtiva() != null ? createDTO.getAtiva() : true)
                .build();

        categoria = categoriaRepository.save(categoria);
        log.info("Categoria criada: ID={}, Nome={}", categoria.getId(), categoria.getNome());

        return CategoriaResponseDTO.from(categoria);
    }


    @Transactional
    public CategoriaResponseDTO atualizar(Integer id, CategoriaUpdateDTO updateDTO) throws Exception {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new Exception("Categoria não encontrada"));

        if (updateDTO.getNome() != null &&
                !categoria.getNome().equalsIgnoreCase(updateDTO.getNome()) &&
                categoriaRepository.existsByNomeIgnoreCase(updateDTO.getNome())) {
            throw new Exception("Já existe uma categoria com este nome");
        }

        if (updateDTO.getNome() != null) categoria.setNome(updateDTO.getNome());
        if (updateDTO.getDescricao() != null) categoria.setDescricao(updateDTO.getDescricao());
        if (updateDTO.getAtiva() != null) categoria.setAtiva(updateDTO.getAtiva());

        categoria = categoriaRepository.save(categoria);
        log.info("Categoria atualizada: ID={}", categoria.getId());

        return CategoriaResponseDTO.from(categoria);
    }


    @Transactional
    public void excluir(Integer id) throws Exception {
        if (!categoriaRepository.existsById(id)) {
            throw new Exception("Categoria não encontrada");
        }

        Long countProdutos = categoriaRepository.countProdutosByCategoria(id);
        if (countProdutos > 0) {
            throw new Exception("Não é possível excluir categoria com " + countProdutos + " produto(s) vinculado(s)");
        }

        categoriaRepository.deleteById(id);
        log.info("Categoria excluída: ID={}", id);
    }
}