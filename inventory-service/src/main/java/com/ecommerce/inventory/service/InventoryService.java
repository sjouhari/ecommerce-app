package com.ecommerce.inventory.service;

import com.ecommerce.shared.dto.InventoryDto;

import java.util.List;

public interface InventoryService {

    List<InventoryDto> getInventoriesByProductId(Long productId);

    boolean checkAvailability(InventoryDto inventoryDto);

    void deductQuantity(InventoryDto inventoryDto);

    InventoryDto createInventory(InventoryDto inventoryDto);

    InventoryDto updateInventory(Long id, InventoryDto inventoryDto);

    String deleteInventory(Long productId);
}
