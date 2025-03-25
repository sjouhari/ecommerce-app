package com.ecommerce.order.service.impl;

import com.ecommerce.order.dto.InventoryDto;
import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(url = "http://localhost:8084/api/inventories", name = "inventory-service")
@Headers("Authorization: {token}")
public interface InventoryApiClient {

    @PostMapping("/checkAvailability")
    boolean checkAvailability(@RequestBody InventoryDto inventoryDto, @RequestHeader("Authorization") String token);

}
