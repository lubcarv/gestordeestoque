// FornecedorCreateDTO.java
package com.luizabau.gestordeestoque.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FornecedorCreateDTO {

    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
    private String nome;

    @Email(message = "Email deve ter formato válido")
    @Size(max = 100, message = "Email deve ter no máximo 100 caracteres")
    private String email;

    @Size(max = 20, message = "Telefone deve ter no máximo 20 caracteres")
    private String fone;

    @Size(max = 14, message = "CNPJ deve ter no máximo 14 caracteres")
    private String cnpj;

    @Size(max = 200, message = "Endereço deve ter no máximo 200 caracteres")
    private String endereco;

    @Builder.Default
    private Boolean ativo = true;
}