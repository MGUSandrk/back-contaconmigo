package com.sistema_contable.sistema.contable.services;

import com.sistema_contable.sistema.contable.model.CostingMethodType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sistema_contable.sistema.contable.model.EntityModel;
import com.sistema_contable.sistema.contable.repository.EntityRepository;
import com.sistema_contable.sistema.contable.services.interfaces.EntityService;

@Service
public class EntityServiceImp implements EntityService {

    //dependencies
    @Autowired
    private EntityRepository entityRepository;

    //GETTERS
    @Override
    @Transactional
    public EntityModel getCurrentEntity() throws Exception {
        EntityModel entity = entityRepository.findFirstEntity();
        
        if (entity == null) {
            throw new RuntimeException("ERROR : No entity found in the system");
        }

        if (entity.getCostingMethod() == null) {
            entity.setCostingMethod(CostingMethodType.FIFO);
            entityRepository.save(entity);
        }

        return entity;
    }
}
