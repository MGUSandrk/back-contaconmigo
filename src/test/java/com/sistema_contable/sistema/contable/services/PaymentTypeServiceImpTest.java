package com.sistema_contable.sistema.contable.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.sistema_contable.sistema.contable.model.accounting.BalanceAccount;
import com.sistema_contable.sistema.contable.model.sales.PaymentType;
import com.sistema_contable.sistema.contable.services.accounting.interfaces.AccountService;

public class PaymentTypeServiceImpTest {

    private PaymentTypeServiceImp service;

    @Mock
    private AccountService accountService;

    @BeforeEach
    void setup() throws Exception {
        MockitoAnnotations.openMocks(this);
        service = new PaymentTypeServiceImp();
        inject("accountService", accountService);
    }

    @Test
    void currentBalanceReturnsTheLastBalanceOfThePaymentTypeAccount() throws Exception {
        PaymentType paymentType = paymentType(4L);

        when(accountService.lastBalance(4L)).thenReturn(12500.0);

        assertEquals(12500.0, service.currentBalance(paymentType));
    }

    @Test
    void currentBalanceReturnsZeroWhenPaymentTypeHasNoAccount() throws Exception {
        PaymentType paymentType = new PaymentType();

        assertEquals(0.0, service.currentBalance(paymentType));
    }

    private PaymentType paymentType(Long accountId) {
        PaymentType paymentType = new PaymentType();
        BalanceAccount account = new BalanceAccount();
        account.setId(accountId);
        paymentType.setAccount(account);
        return paymentType;
    }

    private void inject(String fieldName, Object value) throws Exception {
        Field field = PaymentTypeServiceImp.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(service, value);
    }
}
