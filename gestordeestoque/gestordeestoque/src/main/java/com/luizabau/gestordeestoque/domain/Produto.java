package com.luizabau.gestordeestoque.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "produtos")
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Código é obrigatório")
    @Size(max = 50, message = "Código deve ter no máximo 50 caracteres")
    @Column(name = "codigo", unique = true, nullable = false, length = 50)
    private String codigo;

    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
    @Column(name = "nome", nullable = false, length = 100)
    private String nome;

    @Size(max = 500, message = "Descrição deve ter no máximo 500 caracteres")
    @Column(name = "descricao", length = 500)
    private String descricao;

    @NotNull(message = "Preço é obrigatório")
    @Positive(message = "Preço deve ser positivo")
    @Column(name = "preco", precision = 10, scale = 2)
    private BigDecimal preco;

    @NotBlank(message = "Unidade de medida é obrigatória")
    @Column(name = "unidade_medida", length = 10)
    private String unidadeMedida;

    @Column(name = "dimensoes", length = 100)
    private String dimensoes;

    @Column(name = "cor", length = 30)
    private String cor;

    @Positive(message = "Quantidade mínima deve ser maior que zero")
    @Builder.Default
    @Column(name = "quantidade_minima", nullable = false)
    private Integer quantidadeMinima = 1;

    @PositiveOrZero(message = "Quantidade ideal deve ser zero ou positiva")
    @Column(name = "quantidade_ideal")
    private Integer quantidadeIdeal;

    @PositiveOrZero(message = "Quantidade máxima deve ser zero ou positiva")
    @Column(name = "quantidade_maxima")
    private Integer quantidadeMaxima;

    @Builder.Default
    @Column(name = "ativo")
    private Boolean ativo = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Categoria categoria;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fornecedor_id")
    @JsonIgnoreProperties({"produtos", "hibernateLazyInitializer", "handler"})
    private Fornecedor fornecedor;

    @OneToOne(mappedBy = "produto", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Estoque estoque;

    @Column(name = "data_criacao")
    private LocalDateTime dataCriacao;

    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;

    @Column(name = "usuario_criacao")
    private String usuarioCriacao;

    @Column(name = "usuario_atualizacao")
    private String usuarioAtualizacao;

    @PrePersist
    protected void onCreate() {
        dataCriacao = LocalDateTime.now();
        dataAtualizacao = LocalDateTime.now();
        usuarioCriacao = "lubcarv";

        if (estoque == null) {
            this.estoque = new Estoque();
            this.estoque.setProduto(this);
            this.estoque.setQuantidade(0);
            this.estoque.setQuantidadeMinima(this.quantidadeMinima);
            this.estoque.setQuantidadeIdeal(this.quantidadeIdeal);
            this.estoque.setQuantidadeMaxima(this.quantidadeMaxima);
            this.estoque.atualizarSituacao();        }
    }

    @PreUpdate
    protected void onUpdate() {
        dataAtualizacao = LocalDateTime.now();
        usuarioAtualizacao = "lubcarv";

        if (estoque != null) {
            estoque.setQuantidadeMinima(this.quantidadeMinima);
            estoque.setQuantidadeIdeal(this.quantidadeIdeal);
            estoque.setQuantidadeMaxima(this.quantidadeMaxima);
            estoque.atualizarSituacao();
        }
    }

    public boolean isPrecisoReposicao() {
        return estoque != null && estoque.getQuantidade() <= quantidadeMinima;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Produto produto = (Produto) obj;
        return Objects.equals(id, produto.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}