package com.sistema_contable.sistema.contable.services.accounting.interfaces;

import com.sistema_contable.sistema.contable.model.accounting.Account;

public interface MovementService {
    public boolean existMovementByAccount(Account account)throws Exception;
}
