package com.ecommerce.shared.exception;

public class StockInsufficientException extends RuntimeException {

    public StockInsufficientException(Long productId, String size, String color) {
        super(String.format("Stock for product with id: %d and size %s and color %s is insufficient", productId, size, color));
    }

}
