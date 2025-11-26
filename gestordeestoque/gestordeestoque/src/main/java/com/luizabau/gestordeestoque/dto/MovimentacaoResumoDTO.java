package com.luizabau.gestordeestoque.dto;

import com.luizabau.gestordeestoque.domain.TipoMovimentacao;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok. NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MovimentacaoResumoDTO {
    private Long movimentacaoId;
    private LocalDateTime dataHora;
    private TipoMovimentacao tipo;
    private String tipoDescricao;
    private Integer produtoId;
    private String codigoProduto;
    private String nomeProduto;
    private Integer quantidade;
    private String usuario;
    private String observacao;
}