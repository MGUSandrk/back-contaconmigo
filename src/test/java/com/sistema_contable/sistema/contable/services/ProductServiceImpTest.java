package com.sistema_contable.sistema.contable.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.sistema_contable.sistema.contable.model.Lot;
import com.sistema_contable.sistema.contable.model.Product;
import com.sistema_contable.sistema.contable.model.User;
import com.sistema_contable.sistema.contable.model.accounting.BalanceAccount;
import com.sistema_contable.sistema.contable.model.sales.Payment;
import com.sistema_contable.sistema.contable.model.sales.PaymentType;
import com.sistema_contable.sistema.contable.repository.ProductRepository;

public class ProductServiceImpTest {

    @Test
    void totalStockReturnsTheSumOfProductLotsStock() {
        ProductServiceImp service = new ProductServiceImp();
        Product product = new Product();
        product.setLots(List.of(lot(7), lot(5), lot(0)));

        assertEquals(12, service.totalStock(product));
    }

    @Test
    void totalStockReturnsZeroWhenProductHasNoLots() {
        ProductServiceImp service = new ProductServiceImp();
        Product product = new Product();

        assertEquals(0, service.totalStock(product));
    }

    @Test
    void createWithPaymentsSavesProductAndCreatesLotAccounting() throws Exception {
        ProductServiceImp service = new ProductServiceImp();
        ProductRepository repository = Mockito.mock(ProductRepository.class);
        GoodsAccountingService goodsAccountingService = Mockito.mock(GoodsAccountingService.class);
        inject(service, "repository", repository);
        inject(service, "goodsAccountingService", goodsAccountingService);

        Product product = product("lapicera", 150.0);
        Lot lot = lot(80.0, 10);
        product.addLot(lot);
        List<Payment> payments = List.of(payment(800.0));
        User user = new User();
        when(repository.searchByName("Lapicera")).thenReturn(null);
        when(repository.save(product)).thenReturn(product);

        service.create(product, payments, user);

        assertEquals(product, lot.getProduct());
        verify(repository).save(product);
        verify(goodsAccountingService).purchaseLotAccounting(payments, product, lot, user);
    }

    @Test
    void addLotAssociatesLotAndCreatesAccountingOnlyForNewLot() throws Exception {
        ProductServiceImp service = new ProductServiceImp();
        ProductRepository repository = Mockito.mock(ProductRepository.class);
        GoodsAccountingService goodsAccountingService = Mockito.mock(GoodsAccountingService.class);
        inject(service, "repository", repository);
        inject(service, "goodsAccountingService", goodsAccountingService);

        Product product = product("Lapicera", 150.0);
        product.setId(3L);
        Lot newLot = lot(90.0, 5);
        List<Payment> payments = List.of(payment(450.0));
        User user = new User();
        when(repository.searchById(3L)).thenReturn(product);
        when(repository.save(product)).thenReturn(product);

        Product result = service.addLot(3L, newLot, payments, user);

        assertEquals(product, result);
        assertEquals(product, newLot.getProduct());
        assertEquals(1, product.getLots().size());
        verify(repository).save(product);
        verify(goodsAccountingService).purchaseLotAccounting(payments, product, newLot, user);
    }

    private Product product(String name, Double salePrice) {
        Product product = new Product();
        product.setName(name);
        product.setSalePrice(salePrice);
        return product;
    }

    private Lot lot(Double unitPrice, Integer stock) {
        Lot lot = new Lot();
        lot.setUnitPrice(unitPrice);
        lot.setStock(stock);
        return lot;
    }

    private Lot lot(Integer stock) {
        Lot lot = new Lot();
        lot.setStock(stock);
        return lot;
    }

    private Payment payment(Double amount) {
        Payment payment = new Payment();
        PaymentType paymentType = new PaymentType();
        paymentType.setAccount(new BalanceAccount());
        payment.setPaymentType(paymentType);
        payment.setAmount(amount);
        return payment;
    }

    private void inject(Object target, String fieldName, Object value) throws Exception {
        Field field = ProductServiceImp.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }
}
