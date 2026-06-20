package com.sistema_contable.sistema.contable.exceptions;

import org.springframework.http.HttpStatus;

public class BadSaleException extends ModelExceptions {

    public BadSaleException(String message) {
        super(message);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}
