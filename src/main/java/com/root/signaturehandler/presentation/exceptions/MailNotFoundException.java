package com.root.signaturehandler.presentation.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class MailNotFoundException extends RuntimeException {
    public MailNotFoundException(String msg) {
        super(msg);
    }
}
