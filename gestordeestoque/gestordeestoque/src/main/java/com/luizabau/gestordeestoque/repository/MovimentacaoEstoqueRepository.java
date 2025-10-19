package com.luizabau.gestordeestoque.repository;

import com.luizabau.gestordeestoque.domain.MovimentacaoEstoque;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MovimentacaoEstoqueRepository extends JpaRepository<MovimentacaoEstoque, Long> {

    @Query("SELECT m FROM MovimentacaoEstoque m " +
            "WHERE m.produto.id = :produtoId " +
            "ORDER BY m.dataMovimentacao DESC")
    List<MovimentacaoEstoque> findHistoricoPorProduto(@Param("produtoId") Integer produtoId);
}