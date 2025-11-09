package org.teste.casetecnico.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.teste.casetecnico.model.Produto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface ProdutoRepository extends JpaRepository<Produto, UUID> {

    List<Produto> findAllByOrderByNomeAsc();

    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN TRUE ELSE FALSE END " +
            "FROM Produto p " +
            "WHERE UPPER(p.nome) = UPPER(:nome) " +
            "AND (:id IS NULL OR p.id <> :id)")
    boolean existsByNomeIgnoreCaseAndIdNot(@Param("nome") String nome, @Param("id") UUID id);
}
