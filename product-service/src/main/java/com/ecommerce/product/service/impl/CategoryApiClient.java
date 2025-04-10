package com.ecommerce.product.service.impl;

import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient("CATEGORY-SERVICE")
@Headers("Authorization: {token}")
public interface CategoryApiClient {

    @GetMapping("/api/subcategories/existsById/{id}")
    boolean categoryExistsById(@PathVariable Long id, @RequestHeader("Authorization") String token);

    @GetMapping("/api/sizes/existsByLibelleAndSubCategoryId/{name}/{subCategoryId}")
    boolean sizeExistsByLibelleAndSubCategoryId(@PathVariable String name, @PathVariable Long subCategoryId, @RequestHeader("Authorization") String token);

}
