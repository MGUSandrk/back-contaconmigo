package com.sistema_contable.sistema.contable.model.consting_method;

import jakarta.persistence.*;

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
