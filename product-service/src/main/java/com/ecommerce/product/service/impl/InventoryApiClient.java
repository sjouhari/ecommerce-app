package com.ecommerce.product.service.impl;

import com.ecommerce.shared.dto.InventoryDto;
import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient("INVENTORY-SERVICE")
@Headers("Authorization: {token}")
public interface InventoryApiClient {

    @PostMapping("/api/inventories")
    InventoryDto createInventory(@RequestBody InventoryDto inventoryDto, @RequestHeader("Authorization") String token);

    @PutMapping("/api/inventories/{id}")
    InventoryDto updateInventory(@PathVariable Long id,@RequestBody InventoryDto inventoryDto, @RequestHeader("Authorization") String token);

    @GetMapping("/api/inventories/product/{productId}")
    List<InventoryDto> getInventoriesByProductId(@PathVariable Long productId);
}
