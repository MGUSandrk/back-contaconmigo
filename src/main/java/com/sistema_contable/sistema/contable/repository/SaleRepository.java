package com.sistema_contable.sistema.contable.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sistema_contable.sistema.contable.model.sales.Sale;

import java.util.List;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Long> {

    List<Sale> findByClientId(Long clientId);

}
