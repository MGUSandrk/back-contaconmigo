package com.sistema_contable.sistema.contable.dto;

import java.util.List;

public class SaleRequestDTO {

    private Long idClient;
    private List<SaleProductRequestDTO> products;
    private List<PaymentRequestDTO> payments;

    public Long getIdClient() {
        return idClient;
    }

    public void setIdClient(Long idClient) {
        this.idClient = idClient;
    }

    public List<SaleProductRequestDTO> getProducts() {
        return products;
    }

    public void setProducts(List<SaleProductRequestDTO> products) {
        this.products = products;
    }

    public List<PaymentRequestDTO> getPayments() {
        return payments;
    }

    public void setPayments(List<PaymentRequestDTO> payments) {
        this.payments = payments;
    }
}
