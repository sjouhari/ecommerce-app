package com.ecommerce.inventory.service;

import com.ecommerce.inventory.dto.InventoryDto;

public interface InventoryService {

    boolean checkAvailability(InventoryDto inventoryDto);

    InventoryDto updateQuantity(InventoryDto inventoryDto);

    InventoryDto createInventory(InventoryDto inventoryDto);

    String deleteInventory(Long productId);
}
