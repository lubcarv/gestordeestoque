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

    @Query("SELECT p FROM Produto p " +
            "LEFT JOIN FETCH p.categoria " +
            "LEFT JOIN FETCH p.fornecedor " +
            "LEFT JOIN FETCH p.estoque " +
            "WHERE p.ativo = false")
    List<Produto> findByAtivoFalse();

    @Query("SELECT p FROM Produto p " +
            "LEFT JOIN FETCH p.categoria " +
            "LEFT JOIN FETCH p.fornecedor " +
            "LEFT JOIN FETCH p.estoque " +
            "WHERE p.ativo = true")
    List<Produto> findByAtivoTrue();

    @Query("SELECT DISTINCT p FROM Produto p " +
            "LEFT JOIN FETCH p.categoria " +
            "LEFT JOIN FETCH p.fornecedor " +
            "LEFT JOIN FETCH p.estoque e " +
            "WHERE p.ativo = true " +
            "AND (" +
            "    p.estoque IS NULL " +
            "    OR e.situacao IN ('SEM_ESTOQUE', 'BAIXO')" +
            ")")
    List<Produto> findProdutosComEstoqueBaixo();

    @Query("SELECT COUNT(p) > 0 FROM Produto p WHERE UPPER(p.nome) = UPPER(:nome) AND p.categoria.id = :categoriaId")
    boolean existeNomeECategoria(@Param("nome") String nome, @Param("categoriaId") Integer categoriaId);


    @Query(value = """
        SELECT DISTINCT p.* FROM produtos p
        LEFT JOIN categorias c ON c.id = p.categoria_id
        LEFT JOIN fornecedores f ON f.id = p.fornecedor_id
        LEFT JOIN estoque e ON p.id = e.produto_id
        WHERE (:codigo IS NULL OR CAST(p.codigo AS TEXT) ILIKE CONCAT('%', :codigo, '%'))
          AND (:nome IS NULL OR CAST(p.nome AS TEXT) ILIKE CONCAT('%', :nome, '%'))
          AND (:categoriaId IS NULL OR c.id = :categoriaId)
          AND (:fornecedorId IS NULL OR f.id = :fornecedorId)
          AND (:ativo IS NULL OR p.ativo = :ativo)
        """, nativeQuery = true)
    List<Produto> findByFiltros(
            @Param("codigo") String codigo,
            @Param("nome") String nome,
            @Param("categoriaId") Integer categoriaId,
            @Param("fornecedorId") Integer fornecedorId,
            @Param("ativo") Boolean ativo
    );


    @Query("SELECT p FROM Produto p " +
            "LEFT JOIN FETCH p.categoria " +
            "LEFT JOIN FETCH p.fornecedor " +
            "LEFT JOIN FETCH p.estoque " +
            "WHERE p.codigo = :codigo")
    Optional<Produto> findByCodigo(@Param("codigo") String codigo);
}