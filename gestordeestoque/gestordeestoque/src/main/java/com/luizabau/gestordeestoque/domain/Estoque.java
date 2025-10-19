package com.luizabau.gestordeestoque.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "estoque")
public class Estoque {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;

    @NotNull(message = "Quantidade é obrigatória")
    @PositiveOrZero(message = "Quantidade deve ser zero ou positiva")
    @Builder.Default
    @Column(name = "quantidade", nullable = false)
    private Integer quantidade = 0;

    @Builder.Default
    @Column(name = "quantidade_minima", nullable = false)
    private Integer quantidadeMinima = 0;

    @Column(name = "quantidade_ideal")
    private Integer quantidadeIdeal;

    @Column(name = "quantidade_maxima")
    private Integer quantidadeMaxima;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    @Column(name = "situacao", nullable = false)
    private SituacaoEstoque situacao = SituacaoEstoque.SEM_ESTOQUE;

    @Column(name = "data_ultima_movimentacao")
    private LocalDateTime dataUltimaMovimentacao;

    @Column(name = "usuario_ultima_movimentacao")
    private String usuarioUltimaMovimentacao;

    @PrePersist
    protected void onCreate() {
        dataUltimaMovimentacao = LocalDateTime.now();
        usuarioUltimaMovimentacao = "lubcarv";
        atualizarSituacao();
    }

    public MovimentacaoEstoque entrar(int quantidade, String usuario, String motivo) throws Exception {
        if (quantidade <= 0) {
            throw new Exception("Quantidade deve ser positiva");
        }
        this.quantidade += quantidade;
        this.dataUltimaMovimentacao = LocalDateTime.now();
        this.usuarioUltimaMovimentacao = usuario;
        atualizarSituacao();

        return MovimentacaoEstoque.builder()
                .produto(this.produto)
                .tipo("ENTRADA")
                .quantidade(quantidade)
                .build();
    }

    public MovimentacaoEstoque sair(int quantidade, String usuario, String motivo) throws Exception {
        if (quantidade <= 0) {
            throw new Exception("Quantidade deve ser positiva");
        }
        if (this.quantidade < quantidade) {
            throw new Exception("Estoque insuficiente. Disponível: " + this.quantidade);
        }
        this.quantidade -= quantidade;
        this.dataUltimaMovimentacao = LocalDateTime.now();
        this.usuarioUltimaMovimentacao = usuario;
        atualizarSituacao();

        return MovimentacaoEstoque.builder()
                .produto(this.produto)
                .tipo("SAIDA")
                .quantidade(quantidade)
                .build();
    }

    public void atualizarSituacao() {
        if (quantidade == 0) {
            situacao = SituacaoEstoque.SEM_ESTOQUE;
        } else if (quantidade <= quantidadeMinima) {
            situacao = SituacaoEstoque.BAIXO;
        } else if (quantidadeMaxima != null && quantidade >= quantidadeMaxima) {
            situacao = SituacaoEstoque.ALTO;
        } else {
            situacao = SituacaoEstoque.NORMAL;
        }
    }
}