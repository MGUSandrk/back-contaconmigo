package com.sistema_contable.sistema.contable.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sistema_contable.sistema.contable.dto.LotResponseDTO;
import com.sistema_contable.sistema.contable.exceptions.ModelExceptions;
import com.sistema_contable.sistema.contable.model.Lot;
import com.sistema_contable.sistema.contable.services.interfaces.LotService;
import com.sistema_contable.sistema.contable.services.security.interfaces.AuthorizationService;

@RestController
@RequestMapping("/lots")
@CrossOrigin(origins = "${FRONT_URL}")
public class LotResource {

    //dependencies
    @Autowired
    private LotService service;
    @Autowired
    private AuthorizationService authService;

    //endpoints
    @GetMapping(path = "/{id}", produces = "application/json")
    public ResponseEntity<?> getLotById(@RequestHeader("Authorization") String token, @PathVariable Long id) {
        try {
            authService.adminAuthorize(token);
            return new ResponseEntity<>(lotResponse(service.searchById(id)), HttpStatus.OK);
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
