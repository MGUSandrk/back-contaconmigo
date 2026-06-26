package com.sistema_contable.sistema.contable.repository;

import com.sistema_contable.sistema.contable.model.sales.PaymentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentTypeRepository extends JpaRepository<PaymentType, Long> {

    @Query("SELECT p FROM PaymentType p WHERE p.id = :id")
    PaymentType searchById(@Param("id") Long id);

    @Query("SELECT p FROM PaymentType p WHERE p.type = :type")
    PaymentType searchByName(@Param("type") String type);
}
