package com.sistema_contable.sistema.contable.services.interfaces;

import com.sistema_contable.sistema.contable.model.EntityModel;
import com.sistema_contable.sistema.contable.model.costing_method.CostingMethodType;

public interface EntityService {

    EntityModel getEntity() throws Exception;
    CostingMethodType getCostingMethod() throws Exception;
    void modify(EntityModel entity) throws Exception;
    void create(EntityModel entity) throws Exception;
}
