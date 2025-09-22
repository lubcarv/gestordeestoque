package com.luizabau.gestordeestoque.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EstoqueSimpleDTO {
    private Integer quantidade;
    private Integer quantidadeMinima;
    private Integer quantidadeMaxima;
    private String situacao;
    private LocalDateTime dataUltimaMovimentacao;
    private String usuarioUltimaMovimentacao;
    private String nivelEstoque;
    private Boolean estoqueBaixo;
}