package com.sistema_contable.sistema.contable.services.interfaces;

import com.sistema_contable.sistema.contable.model.Lot;

public interface LotService {
    Lot searchById(Long id) throws Exception;
    void delete(Long id) throws Exception;
}
