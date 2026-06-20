package com.sistema_contable.sistema.contable.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sistema_contable.sistema.contable.exceptions.BadLotException;
import com.sistema_contable.sistema.contable.exceptions.BadProductException;
import com.sistema_contable.sistema.contable.exceptions.ProductNotFindException;
import com.sistema_contable.sistema.contable.model.Lot;
import com.sistema_contable.sistema.contable.model.Product;
import com.sistema_contable.sistema.contable.repository.ProductRepository;
import com.sistema_contable.sistema.contable.services.interfaces.ProductService;

@Service
public class ProductServiceImp implements ProductService {

    //dependencies
    @Autowired
    private ProductRepository repository;

    //CRUD
    @Override
    public void create(Product product) throws Exception {
        this.validateProduct(product);
        String name = product.getName().strip();
        String formatName = name.substring(0, 1).toUpperCase() + name.substring(1);
        product.setName(formatName);
        if (this.searchByName(product.getName()) != null) {
            throw new BadProductException("ERROR : Found product with same name");
        }
        
        for (Lot lot : product.getLots()) {
            this.validateLot(lot);
            lot.setProduct(product);
        }
        repository.save(product);
    }

    @Override
    public void delete(Long id) throws Exception {
        if (repository.existsById(id)) {
            repository.deleteById(id);
        } else {
            throw new ProductNotFindException("ERROR : Product not found to DELETE");
        }
    }

    //GETTERS
    @Override
    public List<Product> getAll() throws Exception {
        return repository.findAll();
    }

    //SEARCHES
    @Override
    public Product searchById(Long id) throws Exception {
        Product product = repository.searchById(id);
        if (product == null) {
            throw new ProductNotFindException("ERROR : Product not found by id");
        }
        return product;
    }

    @Override
    public Product searchByName(String name) throws Exception {
        return repository.searchByName(name);
    }

    //SECONDARY METHODS
    private void validateProduct(Product product) throws Exception {
        if (product == null) {
            throw new BadProductException("ERROR : Product is required");
        }
        if (product.getName() == null || product.getName().isBlank()) {
            throw new BadProductException("ERROR : Product name is required");
        }
        if (product.getSalePrice() == null || product.getSalePrice() < 0) {
            throw new BadProductException("ERROR : Product sale price is invalid");
        }
        if (product.getLots() == null || product.getLots().isEmpty()) {
            throw new BadProductException("ERROR : Product needs at least one lot");
        }
    }

    private void validateLot(Lot lot) throws Exception {
        if (lot == null) {
            throw new BadLotException("ERROR : Lot is required");
        }
        if (lot.getUnitPrice() == null || lot.getUnitPrice() < 0) {
            throw new BadLotException("ERROR : Lot unit price is invalid");
        }
        if (lot.getStock() == null || lot.getStock() < 0) {
            throw new BadLotException("ERROR : Lot stock is invalid");
        }
    }

    
}
