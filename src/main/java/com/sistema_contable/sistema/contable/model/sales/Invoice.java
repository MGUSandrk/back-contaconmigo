package com.sistema_contable.sistema.contable.model.sales;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.sistema_contable.sistema.contable.model.Client;
import com.sistema_contable.sistema.contable.model.EntityModel;
import com.sistema_contable.sistema.contable.model.Product;
import com.sistema_contable.sistema.contable.model.User;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "invoices")
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_invoice")
    private Long id;

    @Column(name = "invoice_number")
    private String invoiceNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "invoice_type")
    private InvoiceType invoiceType;

    @Column(name = "date_created")
    private Date dateCreated;

    @Column(name = "client_full_name")
    private String clientFullName;

    @Column(name = "client_cuit")
    private String clientCuit;

    @Column(name = "seller_full_name")
    private String sellerFullName;

    @Column(name = "entity_name")
    private String entityName;

    @Column(name = "entity_cuit")
    private String entityCuit;

    @Column(name = "entity_commercial_address")
    private String entityCommercialAddress;

    @Column(name = "entity_gross_income_number")
    private String entityGrossIncomeNumber;

    @Column(name = "entity_vat_condition")
    private String entityVatCondition;

    @Column(name = "entity_activity_start_date")
    private Date entityActivityStartDate;

    @Column(name = "sales_point")
    private Integer salesPoint;

    @Column(name = "subtotal")
    private Double subtotal;

    @Column(name = "discount_amount")
    private Double discountAmount;

    @Column(name = "total")
    private Double total;

    @Column(name = "payment_method")
    private String paymentMethod;

    @Column(name = "installments")
    private Integer installments;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "invoice_item_invoice_id")
    private List<InvoiceItem> items;

    @Column(name = "costing_method")
    private String costingMethod;

    @Column(name = "cmv_amount")
    private Double cmvAmount;

    public Invoice() {
    }

    public static Invoice fromSale(Sale sale, Client client, User seller, EntityModel entity,
            InvoiceType invoiceType, String paymentMethod, Integer installments, Double subtotal,
            Double discountAmount, Double total, Double cmvAmount) {
        Invoice invoice = new Invoice();
        invoice.setInvoiceNumber("INV-" + sale.getId());
        invoice.setInvoiceType(resolveInvoiceType(invoiceType));
        invoice.setDateCreated(sale.getDateCreated());
        invoice.setClientFullName(client.getFullName());
        invoice.setClientCuit(client.getCuit());
        invoice.setSellerFullName(seller.getUsername());
        invoice.setEntityName(entity.getName());
        invoice.setEntityCuit(entity.getCuit());
        invoice.setEntityCommercialAddress(entity.getCommercialAddress());
        invoice.setEntityGrossIncomeNumber(entity.getGrossIncomeNumber());
        invoice.setEntityVatCondition(entity.getVatCondition() != null ? entity.getVatCondition().name() : null);
        invoice.setEntityActivityStartDate(entity.getActivityStartDate());
        invoice.setSalesPoint(entity.getSalesPoint());
        invoice.setSubtotal(subtotal);
        invoice.setDiscountAmount(discountAmount);
        invoice.setTotal(total);
        invoice.setPaymentMethod(paymentMethod);
        invoice.setInstallments(installments);
        invoice.setItems(invoiceItemsFromSale(sale));
        invoice.setCostingMethod(entity.getCostingMethod() != null ? entity.getCostingMethod().name() : null);
        invoice.setCmvAmount(cmvAmount);
        return invoice;
    }

    private static InvoiceType resolveInvoiceType(InvoiceType invoiceType) {
        if (invoiceType == null) {
            return InvoiceType.B;
        }
        return invoiceType;
    }

    private static List<InvoiceItem> invoiceItemsFromSale(Sale sale) {
        List<InvoiceItem> invoiceItems = new ArrayList<>();
        if (sale.getSaleProducts() == null) {
            return invoiceItems;
        }
        for (SaleProduct saleProduct : sale.getSaleProducts()) {
            Product product = saleProduct.getProduct();
            InvoiceItem invoiceItem = new InvoiceItem();
            invoiceItem.setProductId(product != null ? product.getId() : null);
            invoiceItem.setProductName(product != null ? product.getName() : null);
            invoiceItem.setQuantity(saleProduct.getQuantity());
            invoiceItem.setUnitPrice(saleProduct.getPrice());
            invoiceItem.setSubtotal(itemSubtotal(saleProduct));
            invoiceItems.add(invoiceItem);
        }
        return invoiceItems;
    }

    private static Double itemSubtotal(SaleProduct saleProduct) {
        if (saleProduct.getPrice() == null || saleProduct.getQuantity() == null) {
            return 0.0;
        }
        return saleProduct.getPrice() * saleProduct.getQuantity();
    }

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

    public InvoiceType getInvoiceType() {
        return invoiceType;
    }

    public void setInvoiceType(InvoiceType invoiceType) {
        this.invoiceType = invoiceType;
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

    public String getEntityCuit() {
        return entityCuit;
    }

    public void setEntityCuit(String entityCuit) {
        this.entityCuit = entityCuit;
    }

    public String getEntityCommercialAddress() {
        return entityCommercialAddress;
    }

    public void setEntityCommercialAddress(String entityCommercialAddress) {
        this.entityCommercialAddress = entityCommercialAddress;
    }

    public String getEntityGrossIncomeNumber() {
        return entityGrossIncomeNumber;
    }

    public void setEntityGrossIncomeNumber(String entityGrossIncomeNumber) {
        this.entityGrossIncomeNumber = entityGrossIncomeNumber;
    }

    public String getEntityVatCondition() {
        return entityVatCondition;
    }

    public void setEntityVatCondition(String entityVatCondition) {
        this.entityVatCondition = entityVatCondition;
    }

    public Date getEntityActivityStartDate() {
        return entityActivityStartDate;
    }

    public void setEntityActivityStartDate(Date entityActivityStartDate) {
        this.entityActivityStartDate = entityActivityStartDate;
    }

    public Integer getSalesPoint() {
        return salesPoint;
    }

    public void setSalesPoint(Integer salesPoint) {
        this.salesPoint = salesPoint;
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

    public List<InvoiceItem> getItems() {
        return items;
    }

    public void setItems(List<InvoiceItem> items) {
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
