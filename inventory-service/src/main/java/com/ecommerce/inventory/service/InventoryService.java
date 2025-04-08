package com.ecommerce.inventory.service;

import com.ecommerce.inventory.dto.InventoryDto;

import java.util.List;

public interface InventoryService {

    List<InventoryDto> getInventoriesByProductId(Long productId);

    boolean checkAvailability(InventoryDto inventoryDto);

    InventoryDto updateQuantity(InventoryDto inventoryDto);

    InventoryDto createInventory(InventoryDto inventoryDto);

    String deleteInventory(Long productId);
}
