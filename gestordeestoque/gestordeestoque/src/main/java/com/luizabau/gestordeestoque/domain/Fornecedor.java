package com.luizabau.gestordeestoque.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "fornecedores")
public class Fornecedor {

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
    @Column(name = "nome", nullable = false, length = 100)
    private String nome;

    @Email(message = "Email deve ter formato válido")
    @Size(max = 100, message = "Email deve ter no máximo 100 caracteres")
    @Column(name = "email", length = 100)
    private String email;

    @Size(max = 20, message = "Telefone deve ter no máximo 20 caracteres")
    @Column(name = "fone", length = 20)
    private String fone;

    @Size(max = 14, message = "CNPJ deve ter no máximo 14 caracteres")
    @Column(name = "cnpj", length = 14)
    private String cnpj;

    @Size(max = 200, message = "Endereço deve ter no máximo 200 caracteres")
    @Column(name = "endereco", length = 200)
    private String endereco;

    @Builder.Default
    @Column(name = "ativo")
    private Boolean ativo = true;

    @OneToMany(mappedBy = "fornecedor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnoreProperties("fornecedor")
    @Builder.Default
    private List<Produto> produtos = new ArrayList<>();

    @Column(name = "data_criacao")
    private LocalDateTime dataCriacao;

    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;

    @PrePersist
    protected void onCreate() {
        dataCriacao = LocalDateTime.now();
        dataAtualizacao = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        dataAtualizacao = LocalDateTime.now();
    }

    public void add(Produto produto){
        this.produtos.add(produto);
        produto.setFornecedor(this);
    }

    public void remove(Produto produto){
        this.produtos.remove(produto);
        produto.setFornecedor(null);
    }
}