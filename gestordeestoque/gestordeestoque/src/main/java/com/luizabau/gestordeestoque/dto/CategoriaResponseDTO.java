package com.luizabau.gestordeestoque.dto;

import com.luizabau.gestordeestoque.domain.Categoria;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoriaResponseDTO {
    private Integer id;
    private String nome;
    private String descricao;
    private Boolean ativa;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;
    private Integer totalProdutos;


    public static CategoriaResponseDTO from(Categoria categoria) {
        CategoriaResponseDTO dto = new CategoriaResponseDTO();
        dto.id = categoria.getId();
        dto.nome = categoria.getNome();
        dto.descricao = categoria.getDescricao();
        dto.ativa = categoria.getAtiva();
        dto.dataCriacao = categoria.getDataCriacao();
        dto.dataAtualizacao = categoria.getDataAtualizacao();
        dto.totalProdutos = categoria.getProdutos() != null ? categoria.getProdutos().size() : 0;
        return dto;
    }
}