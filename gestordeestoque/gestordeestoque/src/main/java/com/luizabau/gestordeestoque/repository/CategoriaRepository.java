package com.luizabau.gestordeestoque.repository;

import com.luizabau.gestordeestoque.domain.Categoria;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {

    List<Categoria> findByDescricaoContainingIgnoreCase(String descricao);
    Optional<Categoria> findByNomeIgnoreCase(String nome);
    boolean existsByNomeIgnoreCase(String nome);
    List<Categoria> findByAtivaTrue();

    @Query("SELECT COUNT(p) FROM Produto p WHERE p.categoria.id = :categoriaId")
    Long countProdutosByCategoria(@Param("categoriaId") Integer categoriaId);

}