package com.sistema_contable.sistema.contable.model;

import com.sistema_contable.sistema.contable.model.consting_method.CostingMethod;
import jakarta.persistence.*;

import java.util.Date;

@jakarta.persistence.Entity
@Table(name = "entities")
public class Entity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_entity")
    private Long id;

    @Column(name = "entity_name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "entity_costing_method_id")
    private CostingMethod costingMethod;

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

    public CostingMethod getCostingMethod() {
        return costingMethod;
    }

    public void setCostingMethod(CostingMethod costingMethod) {
        this.costingMethod = costingMethod;
    }
}
