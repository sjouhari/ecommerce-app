package com.ecommerce.email;

public class SendEmailFailedException extends RuntimeException {

    public SendEmailFailedException(String message) {
        super(message);
    }

}
