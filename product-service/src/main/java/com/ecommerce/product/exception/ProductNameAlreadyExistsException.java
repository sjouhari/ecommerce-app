package com.ecommerce.product.exception;

public class ProductNameAlreadyExistsException extends RuntimeException {
    public ProductNameAlreadyExistsException(String message) {
        super(message);
    }
}
