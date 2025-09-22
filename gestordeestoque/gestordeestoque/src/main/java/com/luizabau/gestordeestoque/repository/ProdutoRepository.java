package com.luizabau.gestordeestoque.repository;

import com.luizabau.gestordeestoque.domain.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Integer> {

    boolean existsByCodigo(String codigo);

    @Query("SELECT COUNT(p) > 0 FROM Produto p WHERE p.codigo = :codigo AND p.id != :id")
    boolean existsByCodigoAndIdNot(@Param("codigo") String codigo, @Param("id") Integer id);

    @Query("SELECT p FROM Produto p " +
            "LEFT JOIN FETCH p.categoria " +
            "LEFT JOIN FETCH p.fornecedor " +
            "LEFT JOIN FETCH p.estoque " +
            "WHERE p.estoque.quantidade <= p.quantidadeMinima " +
            "AND p.ativo = true")
    List<Produto> findProdutosComEstoqueBaixo();

    @Query("SELECT p FROM Produto p " +
            "LEFT JOIN FETCH p.categoria " +
            "LEFT JOIN FETCH p.fornecedor " +
            "LEFT JOIN FETCH p.estoque " +
            "WHERE p.ativo = true")
    List<Produto> findAllWithRelations();

    @Query("SELECT p FROM Produto p " +
            "LEFT JOIN FETCH p.categoria " +
            "LEFT JOIN FETCH p.fornecedor " +
            "LEFT JOIN FETCH p.estoque " +
            "WHERE p.id = :id")
    Optional<Produto> findByIdWithRelations(@Param("id") Integer id);
}