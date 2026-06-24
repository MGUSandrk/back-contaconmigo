package com.sistema_contable.sistema.contable.dto;

import com.sistema_contable.sistema.contable.model.costing_method.CostingMethodType;

public class EntityRequestDTO {

    private String name;
    private CostingMethodType costingMethod;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CostingMethodType getCostingMethod() {
        return costingMethod;
    }

    public void setCostingMethod(CostingMethodType costingMethod) {
        this.costingMethod = costingMethod;
    }
}
