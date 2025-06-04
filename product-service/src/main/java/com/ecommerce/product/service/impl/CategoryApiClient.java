package com.ecommerce.product.service.impl;

import com.ecommerce.product.dto.SubCategoryDto;
import com.ecommerce.shared.dto.CategoryDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("CATEGORY-SERVICE")
public interface CategoryApiClient {

    @GetMapping("/api/categories/existsById/{id}")
    boolean categoryExistsById(@PathVariable Long id);

    @GetMapping("/api/subcategories/existsById/{id}")
    boolean subCategoryExistsById(@PathVariable Long id);

    @GetMapping("/api/subcategories/{id}")
    SubCategoryDto getSubCategoryById(@PathVariable Long id);

    @GetMapping("/api/categories/{id}")
    CategoryDto getCategoryById(@PathVariable Long id);

    @GetMapping("/api/sizes/existsByLibelleAndCategoryId/{name}/{categoryId}")
    boolean sizeExistsByLibelleAndCategoryId(@PathVariable String name, @PathVariable Long categoryId);

}
