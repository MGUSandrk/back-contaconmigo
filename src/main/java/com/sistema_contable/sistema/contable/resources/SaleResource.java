package com.sistema_contable.sistema.contable.resources;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sistema_contable.sistema.contable.dto.PaymentRequestDTO;
import com.sistema_contable.sistema.contable.dto.SaleProductRequestDTO;
import com.sistema_contable.sistema.contable.dto.SaleRequestDTO;
import com.sistema_contable.sistema.contable.exceptions.ModelExceptions;
import com.sistema_contable.sistema.contable.model.Client;
import com.sistema_contable.sistema.contable.model.Product;
import com.sistema_contable.sistema.contable.model.User;
import com.sistema_contable.sistema.contable.model.sales.Payment;
import com.sistema_contable.sistema.contable.model.sales.PaymentType;
import com.sistema_contable.sistema.contable.model.sales.Sale;
import com.sistema_contable.sistema.contable.model.sales.SaleProduct;
import com.sistema_contable.sistema.contable.services.interfaces.SaleService;
import com.sistema_contable.sistema.contable.services.security.interfaces.AuthorizationService;

@RestController
@RequestMapping("/sales")
@CrossOrigin(origins = "${FRONT_URL}")
public class SaleResource {

    //dependencies
    @Autowired
    private SaleService service;
    @Autowired
    private AuthorizationService authService;

    //endpoints
    @PostMapping(path = "/create")
    public ResponseEntity<?> create(@RequestHeader("Authorization") String token, @RequestBody SaleRequestDTO saleDTO) {
        try {
            User seller = authService.authorize(token);
            service.create(saleRequest(saleDTO), seller);
            return new ResponseEntity<>(null, HttpStatus.CREATED);
        } catch (ModelExceptions modelError) {
            System.out.println(modelError.getMessage());
            return new ResponseEntity<>(null, modelError.getHttpStatus());
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //secondary methods
    private Sale saleRequest(SaleRequestDTO dto) {
        if (dto == null) {
            return null;
        }
        Sale sale = new Sale();
        Client client = new Client();
        client.setId(dto.getIdClient());
        sale.setClient(client);
        sale.setSaleProducts(saleProductRequest(dto.getProducts()));
        sale.setPayments(paymentRequest(dto.getPayments()));
        return sale;
    }

    private List<SaleProduct> saleProductRequest(List<SaleProductRequestDTO> dtos) {
        if (dtos == null) {
            return null;
        }
        List<SaleProduct> saleProducts = new ArrayList<>();
        for (SaleProductRequestDTO dto : dtos) {
            saleProducts.add(saleProductRequest(dto));
        }
        return saleProducts;
    }

    private SaleProduct saleProductRequest(SaleProductRequestDTO dto) {
        if (dto == null) {
            return null;
        }
        SaleProduct saleProduct = new SaleProduct();
        Product product = new Product();
        product.setId(dto.getIdProduct());
        saleProduct.setProduct(product);
        saleProduct.setQuantity(dto.getQuantity());
        return saleProduct;
    }

    private List<Payment> paymentRequest(List<PaymentRequestDTO> dtos) {
        if (dtos == null) {
            return null;
        }
        List<Payment> payments = new ArrayList<>();
        for (PaymentRequestDTO dto : dtos) {
            payments.add(paymentRequest(dto));
        }
        return payments;
    }

    private Payment paymentRequest(PaymentRequestDTO dto) {
        if (dto == null) {
            return null;
        }
        Payment payment = new Payment();
        PaymentType paymentType = new PaymentType();
        paymentType.setId(dto.getId());
        payment.setPaymentType(paymentType);
        payment.setAmount(dto.getAmount());
        return payment;
    }
}
