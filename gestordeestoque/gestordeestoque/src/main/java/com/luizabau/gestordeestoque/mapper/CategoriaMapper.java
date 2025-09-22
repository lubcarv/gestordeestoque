package com.luizabau.gestordeestoque.mapper;

import com.luizabau.gestordeestoque.domain.Categoria;
import com.luizabau.gestordeestoque.dto.CategoriaCreateDTO;
import com.luizabau.gestordeestoque.dto.CategoriaResponseDTO;
import com.luizabau.gestordeestoque.dto.CategoriaSimpleDTO;
import com.luizabau.gestordeestoque.repository.CategoriaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CategoriaMapper {

    private final CategoriaRepository categoriaRepository;

    public Categoria toEntity(CategoriaCreateDTO dto) {
        return Categoria.builder()
                .nome(dto.getNome())
                .descricao(dto.getDescricao())
                .ativa(dto.getAtiva())
                .build();
    }

    public CategoriaResponseDTO toResponseDTO(Categoria entity) {
        return CategoriaResponseDTO.builder()
                .id(entity.getId())
                .nome(entity.getNome())
                .descricao(entity.getDescricao())
                .ativa(entity.getAtiva())
                .dataCriacao(entity.getDataCriacao())
                .dataAtualizacao(entity.getDataAtualizacao())
                .totalProdutos(categoriaRepository.countProdutosByCategoria(entity.getId()).intValue())
                .build();
    }

    public CategoriaSimpleDTO toSimpleDTO(Categoria entity) {
        return new CategoriaSimpleDTO(
                entity.getId(),
                entity.getNome(),
                entity.getDescricao()
        );
    }
}