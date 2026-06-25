package com.sistema_contable.sistema.contable.model;

import java.util.Date;

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

    @Column(name = "entity_cuit")
    private String cuit;

    @Column(name = "entity_commercial_address")
    private String commercialAddress;

    @Column(name = "entity_gross_income_number")
    private String grossIncomeNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "entity_vat_condition")
    private VatCondition vatCondition;

    @Column(name = "entity_activity_start_date")
    private Date activityStartDate;

    @Column(name = "entity_sales_point")
    private Integer salesPoint;

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

    public String getCuit() {
        return cuit;
    }

    public void setCuit(String cuit) {
        this.cuit = cuit;
    }

    public String getCommercialAddress() {
        return commercialAddress;
    }

    public void setCommercialAddress(String commercialAddress) {
        this.commercialAddress = commercialAddress;
    }

    public String getGrossIncomeNumber() {
        return grossIncomeNumber;
    }

    public void setGrossIncomeNumber(String grossIncomeNumber) {
        this.grossIncomeNumber = grossIncomeNumber;
    }

    public VatCondition getVatCondition() {
        return vatCondition;
    }

    public void setVatCondition(VatCondition vatCondition) {
        this.vatCondition = vatCondition;
    }

    public Date getActivityStartDate() {
        return activityStartDate;
    }

    public void setActivityStartDate(Date activityStartDate) {
        this.activityStartDate = activityStartDate;
    }

    public Integer getSalesPoint() {
        return salesPoint;
    }

    public void setSalesPoint(Integer salesPoint) {
        this.salesPoint = salesPoint;
    }

}
