package com.sistema_contable.sistema.contable.repository;

import com.sistema_contable.sistema.contable.model.Lot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LotRepository extends JpaRepository<Lot, Long> {

    @Query("SELECT l FROM Lot l WHERE l.id = :id")
    Lot searchById(@Param("id") Long id);
}
