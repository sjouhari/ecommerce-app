package com.ecommerce.order.exception;

public class InvalidPaymentMethodException extends RuntimeException {

    public InvalidPaymentMethodException(String message) {
        super(message);
    }

}
