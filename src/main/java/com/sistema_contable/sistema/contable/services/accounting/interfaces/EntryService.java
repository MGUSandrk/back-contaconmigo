package com.sistema_contable.sistema.contable.services.accounting.interfaces;

import com.sistema_contable.sistema.contable.model.accounting.Entry;
import com.sistema_contable.sistema.contable.model.User;

public interface EntryService {
    void create(Entry entry, User userDB)throws Exception;
}
