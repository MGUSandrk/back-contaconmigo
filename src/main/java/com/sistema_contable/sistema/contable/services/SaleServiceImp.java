package com.sistema_contable.sistema.contable.services;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sistema_contable.sistema.contable.exceptions.BadSaleException;
import com.sistema_contable.sistema.contable.model.Client;
import com.sistema_contable.sistema.contable.model.Product;
import com.sistema_contable.sistema.contable.model.User;
import com.sistema_contable.sistema.contable.model.sales.Payment;
import com.sistema_contable.sistema.contable.model.sales.PaymentType;
import com.sistema_contable.sistema.contable.model.sales.Sale;
import com.sistema_contable.sistema.contable.model.sales.SaleProduct;
import com.sistema_contable.sistema.contable.repository.SaleRepository;
import com.sistema_contable.sistema.contable.services.interfaces.ClientService;
import com.sistema_contable.sistema.contable.services.interfaces.PaymentTypeService;
import com.sistema_contable.sistema.contable.services.interfaces.ProductService;
import com.sistema_contable.sistema.contable.services.interfaces.SaleService;

@Service
public class SaleServiceImp implements SaleService {

    //dependencies
    @Autowired
    private SaleRepository repository;
    @Autowired
    private ClientService clientService;
    @Autowired
    private ProductService productService;
    @Autowired
    private PaymentTypeService paymentTypeService;

    //CRUD
    @Override
    public void create(Sale sale, User seller) throws Exception {
        this.validateSale(sale, seller);
        sale.setClient(this.searchClient(sale));
        sale.setSeller(seller);
        sale.setDateCreated(new Date());
        sale.setTotalPrice(this.configureSaleProducts(sale));
        this.configurePayments(sale);
        this.validatePaymentTotal(sale);
        repository.save(sale);
    }

    //SECONDARY METHODS
    private void validateSale(Sale sale, User seller) throws Exception {
        if (sale == null) {
            throw new BadSaleException("ERROR : Sale is required");
        }
        if (seller == null) {
            throw new BadSaleException("ERROR : Sale seller is required");
        }
        if (sale.getClient() == null || sale.getClient().getId() == null) {
            throw new BadSaleException("ERROR : Sale client is required");
        }
        if (sale.getSaleProducts() == null || sale.getSaleProducts().isEmpty()) {
            throw new BadSaleException("ERROR : Sale needs at least one product");
        }
        if (sale.getPayments() == null || sale.getPayments().isEmpty()) {
            throw new BadSaleException("ERROR : Sale needs at least one payment");
        }
    }

    private Client searchClient(Sale sale) throws Exception {
        return clientService.searchById(sale.getClient().getId());
    }

    private Double configureSaleProducts(Sale sale) throws Exception {
        Double totalPrice = 0.0;
        for (SaleProduct saleProduct : sale.getSaleProducts()) {
            this.validateSaleProduct(saleProduct);
            Product product = productService.searchById(saleProduct.getProduct().getId());
            this.validateProductPrice(product);
            saleProduct.setProduct(product);
            saleProduct.setPrice(product.getSalePrice());
            totalPrice += saleProduct.getPrice() * saleProduct.getQuantity();
        }
        return totalPrice;
    }

    private void validateSaleProduct(SaleProduct saleProduct) throws Exception {
        if (saleProduct == null) {
            throw new BadSaleException("ERROR : Sale product is required");
        }
        if (saleProduct.getProduct() == null || saleProduct.getProduct().getId() == null) {
            throw new BadSaleException("ERROR : Sale product id is required");
        }
        if (saleProduct.getQuantity() == null || saleProduct.getQuantity() <= 0) {
            throw new BadSaleException("ERROR : Sale product quantity is invalid");
        }
    }

    private void validateProductPrice(Product product) throws Exception {
        if (product.getSalePrice() == null || product.getSalePrice() < 0) {
            throw new BadSaleException("ERROR : Product sale price is invalid");
        }
    }

    private void configurePayments(Sale sale) throws Exception {
        for (Payment payment : sale.getPayments()) {
            this.validatePayment(payment);
            PaymentType paymentType = paymentTypeService.searchById(payment.getPaymentType().getId());
            payment.setPaymentType(paymentType);
        }
    }

    private void validatePayment(Payment payment) throws Exception {
        if (payment == null) {
            throw new BadSaleException("ERROR : Payment is required");
        }
        if (payment.getPaymentType() == null || payment.getPaymentType().getId() == null) {
            throw new BadSaleException("ERROR : Payment type is required");
        }
        if (payment.getAmount() == null || payment.getAmount() <= 0) {
            throw new BadSaleException("ERROR : Payment amount is invalid");
        }
    }

    private void validatePaymentTotal(Sale sale) throws Exception {
        Double paymentTotal = sale.getPayments().stream().mapToDouble(Payment::getAmount).sum();
        if (Math.abs(paymentTotal - sale.getTotalPrice()) > 0.01) {
            throw new BadSaleException("ERROR : Sale payment total does not match sale total");
        }
    }
}
