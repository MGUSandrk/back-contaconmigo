package com.sistema_contable.sistema.contable.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sistema_contable.sistema.contable.model.EntityModel;

@Repository
public interface EntityRepository extends JpaRepository<EntityModel, Long> {

    @Query("SELECT e FROM EntityModel e WHERE e.id = :id")
    EntityModel searchById(@Param("id") Long id);

    @Query("SELECT e FROM EntityModel e ORDER BY e.id ASC")
    EntityModel findFirstEntity();
}
