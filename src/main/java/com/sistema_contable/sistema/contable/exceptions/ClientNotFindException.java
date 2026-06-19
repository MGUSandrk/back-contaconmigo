package com.sistema_contable.sistema.contable.exceptions;

import org.springframework.http.HttpStatus;

public class ClientNotFindException extends ModelExceptions {

    public ClientNotFindException(String message) {
        super(message);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.NOT_FOUND;
    }
}
