package com.sistema_contable.sistema.contable.services;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.sistema_contable.sistema.contable.model.Lot;
import com.sistema_contable.sistema.contable.model.Product;

public class ProductServiceImpTest {

    @Test
    void totalStockReturnsTheSumOfProductLotsStock() {
        ProductServiceImp service = new ProductServiceImp();
        Product product = new Product();
        product.setLots(List.of(lot(7), lot(5), lot(0)));

        assertEquals(12, service.totalStock(product));
    }

    @Test
    void totalStockReturnsZeroWhenProductHasNoLots() {
        ProductServiceImp service = new ProductServiceImp();
        Product product = new Product();

        assertEquals(0, service.totalStock(product));
    }

    private Lot lot(Integer stock) {
        Lot lot = new Lot();
        lot.setStock(stock);
        return lot;
    }
}
