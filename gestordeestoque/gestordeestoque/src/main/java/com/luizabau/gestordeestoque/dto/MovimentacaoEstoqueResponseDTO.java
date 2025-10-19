package com.luizabau.gestordeestoque.dto;

import com.luizabau.gestordeestoque.domain.MovimentacaoEstoque;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovimentacaoEstoqueResponseDTO {
    private Integer id;
    private Integer produtoId;
    private String produtoCodigo;
    private String produtoNome;
    private String tipo;
    private Integer quantidade;
    private LocalDateTime dataMovimentacao;

    public static MovimentacaoEstoqueResponseDTO from(MovimentacaoEstoque movimentacao) {
        return MovimentacaoEstoqueResponseDTO.builder()
                .id(movimentacao.getId())
                .produtoId(movimentacao.getProduto().getId())
                .produtoCodigo(movimentacao.getProduto().getCodigo())
                .produtoNome(movimentacao.getProduto().getNome())
                .tipo(movimentacao.getTipo())
                .quantidade(movimentacao.getQuantidade())
                .dataMovimentacao(movimentacao.getDataMovimentacao())
                .build();
    }
}