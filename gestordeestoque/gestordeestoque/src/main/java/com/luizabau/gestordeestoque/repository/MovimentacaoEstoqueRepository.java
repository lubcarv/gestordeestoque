package com.luizabau.gestordeestoque.repository;

import com.luizabau.gestordeestoque.domain.MovimentacaoEstoque;
import com.luizabau.gestordeestoque.domain.TipoMovimentacao;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa. repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org. springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MovimentacaoEstoqueRepository extends JpaRepository<MovimentacaoEstoque, Integer> {

    @Query("SELECT m FROM MovimentacaoEstoque m WHERE m.produto.id = :produtoId ORDER BY m.dataMovimentacao DESC")
    List<MovimentacaoEstoque> findHistoricoPorProduto(@Param("produtoId") Integer produtoId);

    List<MovimentacaoEstoque> findByDataMovimentacaoBetween(LocalDateTime inicio, LocalDateTime fim);

    List<MovimentacaoEstoque> findByTipo(TipoMovimentacao tipo);

    List<MovimentacaoEstoque> findByTipoAndDataMovimentacaoBetween(
            TipoMovimentacao tipo, LocalDateTime inicio, LocalDateTime fim);

    List<MovimentacaoEstoque> findAllByOrderByDataMovimentacaoDesc(Pageable pageable);
}