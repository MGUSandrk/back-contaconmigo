package com.sistema_contable.sistema.contable.model.costing_method;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "costing_methods")
public abstract class  CostingMethod {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_costing_method")
    private Long id;

    @Column(name = "name")
    private String name;
}
