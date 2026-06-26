package com.sistema_contable.sistema.contable.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sistema_contable.sistema.contable.model.costing_method.CostingMethod;

@Repository
public interface CostingMethodRepository extends JpaRepository<CostingMethod, Long> {

    @Query("SELECT cm FROM CostingMethod cm WHERE cm.name = :name")
    CostingMethod searchByName(@Param("name") String name);
}
