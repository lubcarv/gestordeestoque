package com.luizabau.gestordeestoque.dto;

import com.luizabau.gestordeestoque.domain.Fornecedor;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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


    public static FornecedorResponseDTO from(Fornecedor fornecedor) {
        FornecedorResponseDTO dto = new FornecedorResponseDTO();
        dto.id = fornecedor.getId();
        dto.nome = fornecedor.getNome();
        dto.email = fornecedor.getEmail();
        dto.fone = fornecedor.getFone();
        dto.cnpj = fornecedor.getCnpj();
        dto.endereco = fornecedor.getEndereco();
        dto.ativo = fornecedor.getAtivo();
        dto.dataCriacao = fornecedor.getDataCriacao();
        dto.dataAtualizacao = fornecedor.getDataAtualizacao();
        dto.totalProdutos = fornecedor.getProdutos() != null ? fornecedor.getProdutos().size() : 0;
        return dto;
    }
}