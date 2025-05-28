package com.ecommerce.inventory.controller;

import com.ecommerce.inventory.service.InventoryService;
import com.ecommerce.shared.dto.InventoryDto;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventories")
@Validated
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<InventoryDto>> getInventoryByProductId(@PathVariable Long productId) {
        return ResponseEntity.ok(inventoryService.getInventoriesByProductId(productId));
    }

    @PostMapping("/checkAvailability")
    public boolean checkAvailability(@RequestBody InventoryDto inventoryDto) {
        return inventoryService.checkAvailability(inventoryDto);
    }

    @PutMapping("/deductQuantity")
    public ResponseEntity<Void> updateQuantity(@RequestBody @Valid InventoryDto inventoryDto) {
        inventoryService.deductQuantity(inventoryDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping
    public ResponseEntity<InventoryDto> createInventory(@RequestBody @Valid InventoryDto inventoryDto) {
        return new ResponseEntity<>(inventoryService.createInventory(inventoryDto), HttpStatus.CREATED);
    }

    @PutMapping("{id}")
    public ResponseEntity<InventoryDto> updateInventory(@PathVariable Long id, @RequestBody @Valid InventoryDto inventoryDto) {
        return ResponseEntity.ok(inventoryService.updateInventory(id, inventoryDto));
    }

    @DeleteMapping("/delete/{productId}")
    public ResponseEntity<String> deleteInventory(@PathVariable Long productId) {
        return ResponseEntity.ok(inventoryService.deleteInventory(productId));
    }

}
