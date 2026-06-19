package com.sistema_contable.sistema.contable.resources;

import com.sistema_contable.sistema.contable.dto.LotRequestDTO;
import com.sistema_contable.sistema.contable.dto.LotResponseDTO;
import com.sistema_contable.sistema.contable.dto.ProductRequestDTO;
import com.sistema_contable.sistema.contable.dto.ProductResponseDTO;
import com.sistema_contable.sistema.contable.exceptions.ModelExceptions;
import com.sistema_contable.sistema.contable.model.Lot;
import com.sistema_contable.sistema.contable.model.Product;
import com.sistema_contable.sistema.contable.services.ProductService;
import com.sistema_contable.sistema.contable.services.security.interfaces.AuthorizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@CrossOrigin(origins = "${FRONT_URL}")
public class ProductResource {

    //dependencies
    @Autowired
    private ProductService service;
    @Autowired
    private AuthorizationService authService;

    //endpoints
    @PostMapping(path = "/create")
    public ResponseEntity<?> create(@RequestHeader("Authorization") String token, @RequestBody ProductRequestDTO productDTO) {
        try {
            authService.adminAuthorize(token);
            service.create(productRequest(productDTO));
            return new ResponseEntity<>(null, HttpStatus.CREATED);
        } catch (ModelExceptions modelError) {
            System.out.println(modelError.getMessage());
            return new ResponseEntity<>(null, modelError.getHttpStatus());
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(produces = "application/json")
    public ResponseEntity<?> getAll(@RequestHeader("Authorization") String token) {
        try {
            authService.adminAuthorize(token);
            return new ResponseEntity<>(productResponse(service.getAll()), HttpStatus.OK);
        } catch (ModelExceptions modelError) {
            System.out.println(modelError.getMessage());
            return new ResponseEntity<>(null, modelError.getHttpStatus());
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@RequestHeader("Authorization") String token, @PathVariable Long id) {
        try {
            authService.adminAuthorize(token);
            service.delete(id);
            return new ResponseEntity<>(null, HttpStatus.OK);
        } catch (ModelExceptions modelError) {
            System.out.println(modelError.getMessage());
            return new ResponseEntity<>(null, modelError.getHttpStatus());
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //secondary methods
    private Product productRequest(ProductRequestDTO dto) {
        if (dto == null) {
            return null;
        }
        Product product = new Product();
        product.setName(dto.getName());
        product.setSalePrice(dto.getSalePrice());
        if (dto.getLots() != null) {
            for (LotRequestDTO lotDTO : dto.getLots()) {
                Lot lot = lotRequest(lotDTO);
                if (lot != null) {
                    product.addLot(lot);
                }
            }
        }
        return product;
    }

    private Lot lotRequest(LotRequestDTO dto) {
        if (dto == null) {
            return null;
        }
        Lot lot = new Lot();
        lot.setUnitPrice(dto.getUnitPrice());
        lot.setStock(dto.getStock());
        return lot;
    }

    private List<ProductResponseDTO> productResponse(List<Product> products) {
        return products.stream().map(this::productResponse).toList();
    }

    private ProductResponseDTO productResponse(Product product) {
        ProductResponseDTO dto = new ProductResponseDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setSalePrice(product.getSalePrice());
        dto.setLots(product.getLots().stream().map(this::lotResponse).toList());
        return dto;
    }

    private LotResponseDTO lotResponse(Lot lot) {
        LotResponseDTO dto = new LotResponseDTO();
        dto.setId(lot.getId());
        dto.setUnitPrice(lot.getUnitPrice());
        dto.setStock(lot.getStock());
        if (lot.getProduct() != null) {
            dto.setProductId(lot.getProduct().getId());
        }
        return dto;
    }
}
