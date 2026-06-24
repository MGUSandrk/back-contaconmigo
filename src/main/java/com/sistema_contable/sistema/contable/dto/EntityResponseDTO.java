package com.sistema_contable.sistema.contable.dto;

import com.sistema_contable.sistema.contable.model.costing_method.CostingMethodType;

public class EntityResponseDTO {

    private Long id;
    private String name;
    private CostingMethodType costingMethod;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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
