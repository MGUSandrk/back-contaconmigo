package com.sistema_contable.sistema.contable.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sistema_contable.sistema.contable.exceptions.CostingMethodNotFoundException;
import com.sistema_contable.sistema.contable.model.EntityModel;
import com.sistema_contable.sistema.contable.model.costing_method.CostingMethod;
import com.sistema_contable.sistema.contable.repository.CostingMethodRepository;
import com.sistema_contable.sistema.contable.repository.EntityRepository;
import com.sistema_contable.sistema.contable.services.interfaces.EntityService;

@Service
public class EntityServiceImp implements EntityService {

    //dependencies
    @Autowired
    private EntityRepository entityRepository;

    @Autowired
    private CostingMethodRepository costingMethodRepository;

    //GETTERS
    @Override
    @Transactional
    public EntityModel getCurrentEntity() throws Exception {
        EntityModel entity = entityRepository.findFirstEntity();
        
        if (entity == null) {
            throw new RuntimeException("ERROR : No entity found in the system");
        }

        if (entity.getCostingMethod() == null) {
            CostingMethod fifo = costingMethodRepository.searchByName("FIFO");
            if (fifo == null) {
                throw new CostingMethodNotFoundException("ERROR : FIFO costing method not found");
            }
            entity.setCostingMethod(fifo);
            entityRepository.save(entity);
        }

        return entity;
    }
}
