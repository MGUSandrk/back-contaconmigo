package com.sistema_contable.sistema.contable.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sistema_contable.sistema.contable.exceptions.BadEntityException;
import com.sistema_contable.sistema.contable.exceptions.EntityNotFindException;
import com.sistema_contable.sistema.contable.model.EntityModel;
import com.sistema_contable.sistema.contable.model.costing_method.CostingMethodType;
import com.sistema_contable.sistema.contable.repository.EntityRepository;
import com.sistema_contable.sistema.contable.services.interfaces.EntityService;

@Service
public class EntityServiceImp implements EntityService {

    //dependencies
    @Autowired
    private EntityRepository repository;

    //CRUD
    @Override
    public void modify(EntityModel entity) throws Exception {
        this.validateEntity(entity);
        EntityModel storedEntity = this.getEntity();
        storedEntity.setName(entity.getName().strip());
        storedEntity.setCostingMethod(entity.getCostingMethod());
        repository.save(storedEntity);
    }

    @Override
    public void create(EntityModel entity) throws Exception {
        this.validateEntity(entity);
        repository.save(entity);
    }

    //GETTERS
    @Override
    public EntityModel getEntity() throws Exception {
        EntityModel entity = repository.searchEntity();
        if (entity == null) {
            throw new EntityNotFindException("ERROR : Entity not found");
        }
        return entity;
    }

    @Override
    public CostingMethodType getCostingMethod() throws Exception {
        return this.getEntity().getCostingMethod();
    }

    //SECONDARY METHODS
    private void validateEntity(EntityModel entity) throws Exception {
        if (entity == null) {
            throw new BadEntityException("ERROR : Entity is required");
        }
        if (entity.getName() == null || entity.getName().isBlank()) {
            throw new BadEntityException("ERROR : Entity name is required");
        }
        if (entity.getCostingMethod() == null) {
            throw new BadEntityException("ERROR : Entity costing method is required");
        }
    }
}
