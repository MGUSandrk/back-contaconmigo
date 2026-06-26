package com.sistema_contable.sistema.contable.exceptions;

import org.springframework.http.HttpStatus;

public class InsufficientStockException extends ModelExceptions {

    public InsufficientStockException(String message) {
        super(message);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}
