package com.sistema_contable.sistema.contable.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sistema_contable.sistema.contable.dto.EntityRequestDTO;
import com.sistema_contable.sistema.contable.dto.EntityResponseDTO;
import com.sistema_contable.sistema.contable.exceptions.ModelExceptions;
import com.sistema_contable.sistema.contable.model.EntityModel;
import com.sistema_contable.sistema.contable.services.interfaces.EntityService;
import com.sistema_contable.sistema.contable.services.security.interfaces.AuthorizationService;

@RestController
@RequestMapping("/entity")
@CrossOrigin(origins = "${FRONT_URL}")
public class EntityResource {

    //dependencies
    @Autowired
    private EntityService service;
    @Autowired
    private AuthorizationService authService;

    //endpoints
    @GetMapping(produces = "application/json")
    public ResponseEntity<?> getEntity(@RequestHeader("Authorization") String token) {
        try {
            authService.adminAuthorize(token);
            return new ResponseEntity<>(entityResponse(service.getEntity()), HttpStatus.OK);
        } catch (ModelExceptions modelError) {
            System.out.println(modelError.getMessage());
            return new ResponseEntity<>(null, modelError.getHttpStatus());
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping
    public ResponseEntity<?> modify(@RequestHeader("Authorization") String token, @RequestBody EntityRequestDTO entityDTO) {
        try {
            authService.adminAuthorize(token);
            service.modify(entityRequest(entityDTO));
            return new ResponseEntity<>(null, HttpStatus.RESET_CONTENT);
        } catch (ModelExceptions modelError) {
            System.out.println(modelError.getMessage());
            return new ResponseEntity<>(null, modelError.getHttpStatus());
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //secondary methods
    private EntityModel entityRequest(EntityRequestDTO dto) {
        if (dto == null) {
            return null;
        }
        EntityModel entity = new EntityModel();
        entity.setName(dto.getName());
        entity.setCostingMethod(dto.getCostingMethod());
        return entity;
    }

    private EntityResponseDTO entityResponse(EntityModel entity) {
        EntityResponseDTO dto = new EntityResponseDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setCostingMethod(entity.getCostingMethod());
        return dto;
    }
}
