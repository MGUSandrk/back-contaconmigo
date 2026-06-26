package com.sistema_contable.sistema.contable.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sistema_contable.sistema.contable.dto.InvoiceResponseDTO;
import com.sistema_contable.sistema.contable.dto.SaleRequestDTO;
import com.sistema_contable.sistema.contable.exceptions.ModelExceptions;
import com.sistema_contable.sistema.contable.model.User;
import com.sistema_contable.sistema.contable.services.interfaces.SaleService;
import com.sistema_contable.sistema.contable.services.security.interfaces.AuthorizationService;

@RestController
@RequestMapping("/sales")
@CrossOrigin(origins = "${FRONT_URL}")
public class SaleResource {

    //dependencies
    @Autowired
    private SaleService saleService;
    @Autowired
    private AuthorizationService authService;

    //endpoints
    @PostMapping
    public ResponseEntity<?> createSale(@RequestHeader("Authorization") String token, @RequestBody SaleRequestDTO saleRequestDTO) {
        try {
            User seller = authService.authorize(token);
            InvoiceResponseDTO invoiceResponse = saleService.createSale(saleRequestDTO, seller);
            return new ResponseEntity<>(invoiceResponse, HttpStatus.CREATED);
        } catch (ModelExceptions modelError) {
            System.out.println(modelError.getMessage());
            return new ResponseEntity<>(null, modelError.getHttpStatus());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
