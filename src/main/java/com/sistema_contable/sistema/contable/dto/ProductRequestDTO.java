package com.sistema_contable.sistema.contable.dto;

import java.util.List;

public class ProductRequestDTO {

    private String name;
    private Double salePrice;
    private List<LotRequestDTO> lots;
    private List<PaymentRequestDTO> payments;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(Double salePrice) {
        this.salePrice = salePrice;
    }

    public List<LotRequestDTO> getLots() {
        return lots;
    }

    public void setLots(List<LotRequestDTO> lots) {
        this.lots = lots;
    }

    public List<PaymentRequestDTO> getPayments() {
        return payments;
    }

    public void setPayments(List<PaymentRequestDTO> payments) {
        this.payments = payments;
    }
}
