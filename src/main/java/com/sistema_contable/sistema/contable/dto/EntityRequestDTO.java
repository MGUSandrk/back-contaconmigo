package com.sistema_contable.sistema.contable.dto;

import java.util.Date;

import com.sistema_contable.sistema.contable.model.VatCondition;
import com.sistema_contable.sistema.contable.model.CostingMethodType;

public class EntityRequestDTO {

    private String name;
    private CostingMethodType costingMethod;
    private String cuit;
    private String commercialAddress;
    private String grossIncomeNumber;
    private VatCondition vatCondition;
    private Date activityStartDate;
    private Integer salesPoint;

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
