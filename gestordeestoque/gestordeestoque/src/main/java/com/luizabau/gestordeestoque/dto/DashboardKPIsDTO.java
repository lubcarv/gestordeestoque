package com.luizabau.gestordeestoque.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardKPIsDTO {
    private BigDecimal valorTotalEstoque;
    private Integer produtosAtivos;
    private Integer produtosEstoqueBaixo;
    private Double taxaGiroEstoque;
    private Integer totalMovimentacoesMes;
    private Integer totalEntradas;
    private Integer totalSaidas;
}