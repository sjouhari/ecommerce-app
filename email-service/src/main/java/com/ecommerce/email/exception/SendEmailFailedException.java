package com.ecommerce.email.exception;

public class SendEmailFailedException extends RuntimeException {

    public SendEmailFailedException(String message) {
        super(message);
    }

}
