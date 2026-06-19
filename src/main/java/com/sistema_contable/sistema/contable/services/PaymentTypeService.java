package com.sistema_contable.sistema.contable.services;

import com.sistema_contable.sistema.contable.model.sales.PaymentType;

import java.util.List;

public interface PaymentTypeService {
    void create(PaymentType paymentType) throws Exception;
    List<PaymentType> getAll() throws Exception;
    PaymentType searchById(Long id) throws Exception;
    void modifyById(Long id, PaymentType paymentType) throws Exception;
    void deleteById(Long id) throws Exception;
}
