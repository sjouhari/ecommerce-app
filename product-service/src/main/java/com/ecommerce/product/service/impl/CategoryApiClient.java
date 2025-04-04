package com.ecommerce.product.service.impl;

import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(url = "http://localhost:8081/api", name = "category-service")
@Headers("Authorization: {token}")
public interface CategoryApiClient {

    @GetMapping("/subcategories/existsById/{id}")
    boolean categoryExistsById(@PathVariable Long id, @RequestHeader("Authorization") String token);

    @GetMapping("/sizes/existsByLibelleAndSubCategoryId/{name}/{subCategoryId}")
    boolean sizeExistsByLibelleAndSubCategoryId(@PathVariable String name, @PathVariable Long subCategoryId, @RequestHeader("Authorization") String token);

}
