package com.ecommerce.order.service.impl;

import com.ecommerce.shared.dto.InventoryDto;
import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient("INVENTORY-SERVICE")
@Headers("Authorization: {token}")
public interface InventoryApiClient {

    @PostMapping("/api/inventories/checkAvailability")
    boolean checkAvailability(@RequestBody InventoryDto inventoryDto, @RequestHeader("Authorization") String token);

}
