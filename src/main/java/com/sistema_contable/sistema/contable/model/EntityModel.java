package com.sistema_contable.sistema.contable.model;

import java.util.Date;
import java.util.List;

import com.sistema_contable.sistema.contable.model.costing_method.CostingMethod;
import com.sistema_contable.sistema.contable.model.sales.Payment;
import com.sistema_contable.sistema.contable.model.sales.Sale;
import com.sistema_contable.sistema.contable.model.sales.SaleProduct;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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

    public void sell(User seller, Client client, List<SaleProduct> products, List<Payment> payments){
        Sale sale = new Sale(new Date(), client, seller, products, payments);
    }
}
