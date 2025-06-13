package com.ecommerce.order.service.impl;

import com.ecommerce.order.dto.ProductDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient("PRODUCT-SERVICE")
public interface ProductApiClient {

    @GetMapping("/api/products/{id}")
    ProductDto getProductById(@PathVariable Long id);

}
