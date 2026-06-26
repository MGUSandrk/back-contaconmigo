package com.sistema_contable.sistema.contable.services.interfaces;

import com.sistema_contable.sistema.contable.model.EntityModel;

public interface EntityService {
    EntityModel getCurrentEntity() throws Exception;
}
