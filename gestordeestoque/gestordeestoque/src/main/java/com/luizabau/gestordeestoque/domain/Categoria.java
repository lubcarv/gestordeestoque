package com.luizabau.gestordeestoque.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "categorias")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Nome é obrigatório")
    @Size(max = 50, message = "Nome deve ter no máximo 50 caracteres")
    @Column(name = "nome", length = 50, nullable = false)
    private String nome;

    @Size(max = 200, message = "Descrição deve ter no máximo 200 caracteres")
    @Column(name = "descricao", length = 200)
    private String descricao;

    @Builder.Default
    @Column(name = "ativa", nullable = false)
    private Boolean ativa = true;

    @Column(name = "data_criacao", nullable = false, updatable = false)
    @Builder.Default
    private LocalDateTime dataCriacao = LocalDateTime.now();

    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;

    @PreUpdate
    protected void onUpdate() {
        this.dataAtualizacao = LocalDateTime.now();
    }
}