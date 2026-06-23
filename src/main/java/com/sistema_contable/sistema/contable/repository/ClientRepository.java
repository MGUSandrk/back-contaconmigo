package com.sistema_contable.sistema.contable.repository;

import com.sistema_contable.sistema.contable.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    @Query("SELECT c FROM Client c WHERE c.id = :id")
    Client searchById(@Param("id") Long id);
}
