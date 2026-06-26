package com.sistema_contable.sistema.contable.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sistema_contable.sistema.contable.model.sales.Invoice;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

}
