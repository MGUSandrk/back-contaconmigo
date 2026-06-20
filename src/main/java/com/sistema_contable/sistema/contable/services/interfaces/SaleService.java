package com.sistema_contable.sistema.contable.services.interfaces;

import com.sistema_contable.sistema.contable.model.User;
import com.sistema_contable.sistema.contable.model.sales.Sale;

public interface SaleService {
    void create(Sale sale, User seller) throws Exception;
}
