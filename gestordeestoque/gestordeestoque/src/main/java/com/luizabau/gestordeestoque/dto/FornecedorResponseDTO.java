// FornecedorResponseDTO.java
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
public class FornecedorResponseDTO {
    private Integer id;
    private String nome;
    private String email;
    private String fone;
    private String cnpj;
    private String endereco;
    private Boolean ativo;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;
    private Integer totalProdutos;
}