package com.luizabau.gestordeestoque.dto;

import lombok. AllArgsConstructor;
import lombok.Builder;
import lombok. Data;
import lombok.NoArgsConstructor;

import java. util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MovimentacoesPeriodoDTO {
    private List<String> labels;
    private List<Integer> entradas;
    private List<Integer> saidas;
}