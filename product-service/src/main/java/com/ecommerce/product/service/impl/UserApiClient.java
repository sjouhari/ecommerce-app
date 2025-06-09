package com.ecommerce.product.service.impl;

import com.ecommerce.shared.dto.StoreDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("USER-SERVICE")
public interface UserApiClient {

    @GetMapping("/api/stores/{id}")
    StoreDto getStore(@PathVariable Long id);

}
