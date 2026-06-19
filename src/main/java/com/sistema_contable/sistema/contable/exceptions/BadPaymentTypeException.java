package com.sistema_contable.sistema.contable.exceptions;

import org.springframework.http.HttpStatus;

public class BadPaymentTypeException extends ModelExceptions {

    public BadPaymentTypeException(String message) {
        super(message);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}
