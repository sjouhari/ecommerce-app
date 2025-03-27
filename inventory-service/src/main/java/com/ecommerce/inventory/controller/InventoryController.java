package com.ecommerce.inventory.controller;

import com.ecommerce.inventory.dto.InventoryDto;
import com.ecommerce.inventory.service.InventoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inventories")
@Validated
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    @PostMapping("/checkAvailability")
    public boolean checkAvailability(@RequestBody InventoryDto inventoryDto) {
        return inventoryService.checkAvailability(inventoryDto);
    }

    @PutMapping("/updateQuantity")
    public ResponseEntity<InventoryDto> updateQuantity(@RequestBody @Valid InventoryDto inventoryDto) {
        return ResponseEntity.ok(inventoryService.updateQuantity(inventoryDto));
    }

    @PostMapping("/create")
    public ResponseEntity<InventoryDto> createInventory(@RequestBody @Valid InventoryDto inventoryDto) {
        return new ResponseEntity<>(inventoryService.createInventory(inventoryDto), HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{productId}")
    public ResponseEntity<String> deleteInventory(@PathVariable Long productId) {
        return ResponseEntity.ok(inventoryService.deleteInventory(productId));
    }

}
