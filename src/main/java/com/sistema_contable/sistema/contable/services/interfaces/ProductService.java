package com.sistema_contable.sistema.contable.services.interfaces;

import com.sistema_contable.sistema.contable.model.Product;

import java.util.List;

public interface ProductService {
    void create(Product product) throws Exception;
    List<Product> getAll() throws Exception;
    Product searchById(Long id) throws Exception;
    Product searchByName(String name) throws Exception;
    void delete(Long id) throws Exception;
}
