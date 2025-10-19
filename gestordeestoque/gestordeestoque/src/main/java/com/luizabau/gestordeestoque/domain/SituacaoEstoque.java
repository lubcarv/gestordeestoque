package com.luizabau.gestordeestoque.domain;

import lombok.Getter;

@Getter
public enum SituacaoEstoque {
    SEM_ESTOQUE("SEM_ESTOQUE"),
    BAIXO("BAIXO"),
    NORMAL("NORMAL"),
    ALTO("ALTO");

    private final String descricao;

    SituacaoEstoque(String descricao) {
        this.descricao = descricao;
    }
}