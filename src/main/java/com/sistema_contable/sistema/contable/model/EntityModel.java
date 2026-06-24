package com.sistema_contable.sistema.contable.model;

import com.sistema_contable.sistema.contable.model.costing_method.CostingMethodType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "entities")
public class EntityModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_entity")
    private Long id;

    @Column(name = "entity_name")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "entity_costing_method")
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
