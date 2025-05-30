package com.ecommerce.product.service.impl;

import com.ecommerce.product.dto.SubCategoryDto;
import com.ecommerce.shared.dto.CategoryDto;
import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient("CATEGORY-SERVICE")
@Headers("Authorization: {token}")
public interface CategoryApiClient {

    @GetMapping("/api/subcategories/existsById/{id}")
    boolean subCategoryExistsById(@PathVariable Long id, @RequestHeader("Authorization") String token);

    @GetMapping("/api/subcategories/{id}")
    SubCategoryDto getSubCategoryById(@PathVariable Long id, @RequestHeader("Authorization") String token);

    @GetMapping("/api/categories/{id}")
    CategoryDto getCategoryById(@PathVariable Long id, @RequestHeader("Authorization") String token);

    @GetMapping("/api/sizes/existsByLibelleAndCategoryId/{name}/{categoryId}")
    boolean sizeExistsByLibelleAndCategoryId(@PathVariable String name, @PathVariable Long categoryId, @RequestHeader("Authorization") String token);

}
