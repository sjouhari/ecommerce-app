package com.ecommerce.product.service.impl;

import com.ecommerce.product.dto.InventoryDto;
import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(url = "http://localhost:8084/api/inventories", name = "inventory-service")
@Headers("Authorization: {token}")
public interface InventoryApiClient {

    @PostMapping("/create")
    public InventoryDto createInventory(@RequestBody InventoryDto inventoryDto, @RequestHeader("Authorization") String token);
}
