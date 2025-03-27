package com.ecommerce.common.exception;

public class StockInsufficientException extends RuntimeException {

    public StockInsufficientException(Long productId, Long sizeId) {
        super(String.format("Stock for product %d and size %d is insufficient", productId, sizeId));
    }

}
