package com.ecommerce.comment.service.impl;

import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient("PRODUCT-SERVICE")
@Headers("Authorization: {token}")
public interface ProductApiClient {

    @GetMapping("/api/products/existsById/{id}")
    boolean productExistsById(@PathVariable Long id, @RequestHeader("Authorization") String token);

}
