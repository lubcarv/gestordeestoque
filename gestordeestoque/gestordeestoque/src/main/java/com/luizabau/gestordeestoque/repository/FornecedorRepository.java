package com.luizabau.gestordeestoque.repository;

import com.luizabau.gestordeestoque.domain.Fornecedor;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FornecedorRepository extends JpaRepository<Fornecedor, Integer> {

    boolean existsByEmail(String email);
    boolean existsByCnpj(String cnpj);

    List<Fornecedor> findByAtivoTrue();

    @Query("SELECT f FROM Fornecedor f WHERE UPPER(f.nome) LIKE UPPER(CONCAT('%', :nome, '%'))")
    List<Fornecedor> findByNomeContainingIgnoreCase(@Param("nome") String nome);

    @Query("SELECT COUNT(p) FROM Produto p WHERE p.fornecedor.id = :fornecedorId")
    Long countProdutosByFornecedor(@Param("fornecedorId") Integer fornecedorId);


}