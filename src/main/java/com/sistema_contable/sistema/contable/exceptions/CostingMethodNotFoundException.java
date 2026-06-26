package com.sistema_contable.sistema.contable.exceptions;

import org.springframework.http.HttpStatus;

public class CostingMethodNotFoundException extends ModelExceptions {

    public CostingMethodNotFoundException(String message) {
        super(message);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.NOT_FOUND;
    }
}
