package com.luizabau.gestordeestoque.exceptions;

public class NegocioEstoqueException extends  RuntimeException {
    public NegocioEstoqueException(String mensage) {
        super(mensage);
    }
}
