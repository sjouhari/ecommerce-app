package com.ecommerce.shared.exception;

import com.ecommerce.shared.dto.InventoryDto;

public class StockNotFoundException extends RuntimeException {
    public StockNotFoundException(InventoryDto inventoryDto) {
        super(String.format("Stock not found for product with id: %d and size %s and color %s", inventoryDto.getProductId(), inventoryDto.getSize(), inventoryDto.getColor().toString()));
    }
}
