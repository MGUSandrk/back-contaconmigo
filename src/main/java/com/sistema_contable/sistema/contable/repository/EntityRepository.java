package com.sistema_contable.sistema.contable.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.sistema_contable.sistema.contable.model.EntityModel;

@Repository
public interface EntityRepository extends JpaRepository<EntityModel, Long> {

    @Query(value = "SELECT * FROM entities ORDER BY id_entity ASC LIMIT 1", nativeQuery = true)
    EntityModel searchEntity();
}
