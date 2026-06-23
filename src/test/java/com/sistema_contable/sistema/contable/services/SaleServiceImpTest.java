package com.sistema_contable.sistema.contable.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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

public class SaleServiceImpTest {

    private SaleServiceImp service;

    @Mock
    private SaleRepository repository;
    @Mock
    private ClientService clientService;
    @Mock
    private ProductService productService;
    @Mock
    private PaymentTypeService paymentTypeService;

    @BeforeEach
    void setup() throws Exception {
        MockitoAnnotations.openMocks(this);
        service = new SaleServiceImp();
        inject("repository", repository);
        inject("clientService", clientService);
        inject("productService", productService);
        inject("paymentTypeService", paymentTypeService);
    }

    @Test
    void createCalculatesSaleProductPricesAndTotalFromStoredProducts() throws Exception {
        Client client = client(1L);
        Product product = product(2L, 1500.0);
        PaymentType paymentType = paymentType(3L);
        User seller = new User();
        Sale sale = sale(1L, 2L, 2, 3L, 3000.0);

        when(clientService.searchById(1L)).thenReturn(client);
        when(productService.searchById(2L)).thenReturn(product);
        when(paymentTypeService.searchById(3L)).thenReturn(paymentType);

        service.create(sale, seller);

        ArgumentCaptor<Sale> captor = ArgumentCaptor.forClass(Sale.class);
        verify(repository).save(captor.capture());
        Sale storedSale = captor.getValue();

        assertEquals(client, storedSale.getClient());
        assertEquals(seller, storedSale.getSeller());
        assertNotNull(storedSale.getDateCreated());
        assertEquals(3000.0, storedSale.getTotalPrice());
        assertEquals(product, storedSale.getSaleProducts().get(0).getProduct());
        assertEquals(1500.0, storedSale.getSaleProducts().get(0).getPrice());
        assertEquals(paymentType, storedSale.getPayments().get(0).getPaymentType());
    }

    @Test
    void createRejectsSaleWhenPaymentTotalDoesNotMatchSaleTotal() throws Exception {
        Sale sale = sale(1L, 2L, 2, 3L, 2000.0);

        when(clientService.searchById(1L)).thenReturn(client(1L));
        when(productService.searchById(2L)).thenReturn(product(2L, 1500.0));
        when(paymentTypeService.searchById(3L)).thenReturn(paymentType(3L));

        assertThrows(BadSaleException.class, () -> service.create(sale, new User()));
    }

    private Sale sale(Long clientId, Long productId, Integer quantity, Long paymentTypeId, Double paymentAmount) {
        Sale sale = new Sale();
        Client client = new Client();
        client.setId(clientId);
        sale.setClient(client);
        SaleProduct saleProduct = new SaleProduct();
        Product product = new Product();
        product.setId(productId);
        saleProduct.setProduct(product);
        saleProduct.setQuantity(quantity);
        sale.setSaleProducts(List.of(saleProduct));
        Payment payment = new Payment();
        PaymentType paymentType = new PaymentType();
        paymentType.setId(paymentTypeId);
        payment.setPaymentType(paymentType);
        payment.setAmount(paymentAmount);
        sale.setPayments(List.of(payment));
        return sale;
    }

    private Client client(Long id) {
        Client client = new Client();
        client.setId(id);
        return client;
    }

    private Product product(Long id, Double salePrice) {
        Product product = new Product();
        product.setId(id);
        product.setSalePrice(salePrice);
        return product;
    }

    private PaymentType paymentType(Long id) {
        PaymentType paymentType = new PaymentType();
        paymentType.setId(id);
        return paymentType;
    }

    private void inject(String fieldName, Object value) throws Exception {
        Field field = SaleServiceImp.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(service, value);
    }
}
