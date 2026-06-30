package com.sistema_contable.sistema.contable.services;

import com.sistema_contable.sistema.contable.model.CostingMethodType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.sistema_contable.sistema.contable.exceptions.BadEntityException;
import com.sistema_contable.sistema.contable.exceptions.EntityNotFindException;
import com.sistema_contable.sistema.contable.model.EntityModel;
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
        storedEntity.setCuit(entity.getCuit().strip());
        storedEntity.setCommercialAddress(entity.getCommercialAddress().strip());
        storedEntity.setGrossIncomeNumber(entity.getGrossIncomeNumber().strip());
        storedEntity.setVatCondition(entity.getVatCondition());
        storedEntity.setActivityStartDate(entity.getActivityStartDate());
        storedEntity.setSalesPoint(entity.getSalesPoint());
        repository.save(storedEntity);
    }

    @Override
    public void create(EntityModel entity) throws Exception {
        this.validateEntity(entity);
        entity.setName(entity.getName().strip());
        entity.setCuit(entity.getCuit().strip());
        entity.setCommercialAddress(entity.getCommercialAddress().strip());
        entity.setGrossIncomeNumber(entity.getGrossIncomeNumber().strip());
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
        if (entity.getCuit() == null || entity.getCuit().isBlank()) {
            throw new BadEntityException("ERROR : Entity cuit is required");
        }
        if (entity.getCommercialAddress() == null || entity.getCommercialAddress().isBlank()) {
            throw new BadEntityException("ERROR : Entity commercial address is required");
        }
        if (entity.getGrossIncomeNumber() == null || entity.getGrossIncomeNumber().isBlank()) {
            throw new BadEntityException("ERROR : Entity gross income number is required");
        }
        if (entity.getVatCondition() == null) {
            throw new BadEntityException("ERROR : Entity vat condition is required");
        }
        if (entity.getActivityStartDate() == null) {
            throw new BadEntityException("ERROR : Entity activity start date is required");
        }
        if (entity.getSalesPoint() == null || entity.getSalesPoint() <= 0) {
            throw new BadEntityException("ERROR : Entity sales point is invalid");
        }
    }
}
