package com.sistema_contable.sistema.contable.dto;

import java.util.Date;
import java.util.List;

public class InvoiceResponseDTO {

    private Long id;
    private String invoiceNumber;
    private Date dateCreated;
    private String clientFullName;
    private String clientCuit;
    private String sellerFullName;
    private String entityName;
    private Double subtotal;
    private Double discountAmount;
    private Double total;
    private String paymentMethod;
    private Integer installments;
    private List<InvoiceItemResponseDTO> items;
    private String costingMethod;
    private Double cmvAmount;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getClientFullName() {
        return clientFullName;
    }

    public void setClientFullName(String clientFullName) {
        this.clientFullName = clientFullName;
    }

    public String getClientCuit() {
        return clientCuit;
    }

    public void setClientCuit(String clientCuit) {
        this.clientCuit = clientCuit;
    }

    public String getSellerFullName() {
        return sellerFullName;
    }

    public void setSellerFullName(String sellerFullName) {
        this.sellerFullName = sellerFullName;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public Double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(Double subtotal) {
        this.subtotal = subtotal;
    }

    public Double getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(Double discountAmount) {
        this.discountAmount = discountAmount;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Integer getInstallments() {
        return installments;
    }

    public void setInstallments(Integer installments) {
        this.installments = installments;
    }

    public List<InvoiceItemResponseDTO> getItems() {
        return items;
    }

    public void setItems(List<InvoiceItemResponseDTO> items) {
        this.items = items;
    }

    public String getCostingMethod() {
        return costingMethod;
    }

    public void setCostingMethod(String costingMethod) {
        this.costingMethod = costingMethod;
    }

    public Double getCmvAmount() {
        return cmvAmount;
    }

    public void setCmvAmount(Double cmvAmount) {
        this.cmvAmount = cmvAmount;
    }
}
