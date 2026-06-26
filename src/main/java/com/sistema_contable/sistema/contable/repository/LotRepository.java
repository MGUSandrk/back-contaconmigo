package com.sistema_contable.sistema.contable.repository;

import java.util.List;

import com.sistema_contable.sistema.contable.model.Lot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LotRepository extends JpaRepository<Lot, Long> {

    @Query("SELECT l FROM Lot l WHERE l.id = :id")
    Lot searchById(@Param("id") Long id);

    @Query("SELECT l FROM Lot l WHERE l.product.id = :productId AND l.stock > 0 ORDER BY l.id ASC")
    List<Lot> findByProductWithStockFIFO(@Param("productId") Long productId);

    @Query("SELECT l FROM Lot l WHERE l.product.id = :productId AND l.stock > 0 ORDER BY l.id DESC")
    List<Lot> findByProductWithStockLIFO(@Param("productId") Long productId);

    @Query("SELECT l FROM Lot l WHERE l.product.id = :productId AND l.stock > 0")
    List<Lot> findByProductWithStock(@Param("productId") Long productId);
}
