package com.sistema_contable.sistema.contable.services.interfaces;

import com.sistema_contable.sistema.contable.dto.InvoiceResponseDTO;
import com.sistema_contable.sistema.contable.dto.SaleRequestDTO;
import com.sistema_contable.sistema.contable.model.User;

public interface SaleService {
    InvoiceResponseDTO createSale(SaleRequestDTO saleRequestDTO, User seller) throws Exception;
}
