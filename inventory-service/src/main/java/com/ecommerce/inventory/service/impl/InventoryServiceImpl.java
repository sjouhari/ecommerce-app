package com.ecommerce.inventory.service.impl;

import com.ecommerce.inventory.dto.InventoryDto;
import com.ecommerce.inventory.entity.Inventory;
import com.ecommerce.inventory.exception.ResourceNotFoundException;
import com.ecommerce.inventory.mapper.InventoryMapper;
import com.ecommerce.inventory.repository.InventoryRepository;
import com.ecommerce.inventory.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventoryServiceImpl implements InventoryService {

    @Autowired
    private InventoryRepository inventoryRepository;

    @Override
    public List<InventoryDto> getInventoriesByProductId(Long productId) {
        List<Inventory> inventories = inventoryRepository.findAllByProductId(productId);
        return InventoryMapper.INSTANCE.inventoryListToInventoryDtoList(inventories);
    }

    @Override
    public boolean checkAvailability(InventoryDto inventoryDto) {
        Inventory inventory = inventoryRepository.findByProductIdAndSizeAndColor(inventoryDto.getProductId(), inventoryDto.getSize(), inventoryDto.getColor().toString()).orElseThrow(
                () -> new ResourceNotFoundException("Inventory", "productId", String.valueOf(inventoryDto.getProductId()))
        );
        return inventory.getQuantity() >= inventoryDto.getQuantity();
    }

    @Override
    public InventoryDto updateQuantity(InventoryDto inventoryDto) {
        Inventory inventory = inventoryRepository.findByProductIdAndSizeAndColor(inventoryDto.getProductId(), inventoryDto.getSize(), inventoryDto.getColor().toString()).orElseThrow(
                () -> new ResourceNotFoundException("Inventory", "productId", String.valueOf(inventoryDto.getProductId()))
        );

        inventory.setQuantity(inventory.getQuantity() + inventoryDto.getQuantity());
        Inventory savedInventory = inventoryRepository.save(inventory);
        return InventoryMapper.INSTANCE.inventoryToInventoryDto(savedInventory);
    }

    @Override
    public InventoryDto createInventory(InventoryDto inventoryDto) {
        Inventory inventory = InventoryMapper.INSTANCE.inventoryDtoToInventory(inventoryDto);
        Inventory savedInventory = inventoryRepository.save(inventory);
        return InventoryMapper.INSTANCE.inventoryToInventoryDto(savedInventory);
    }

    @Override
    public String deleteInventory(Long productId) {
        inventoryRepository.deleteByProductId(productId);
        return "Inventory deleted successfully";
    }
}
