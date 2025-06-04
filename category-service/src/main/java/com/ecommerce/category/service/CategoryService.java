package com.ecommerce.category.service;

import com.ecommerce.category.dto.CategoryRequestDto;

import java.util.List;

public interface CategoryService {

    List<CategoryRequestDto> getAllCategories();

    CategoryRequestDto getCategoryById(Long id);

    CategoryRequestDto createCategory(CategoryRequestDto categoryRequestDto);

    CategoryRequestDto updateCategory(Long id, CategoryRequestDto categoryRequestDto);

    void deleteCategory(Long id);

    boolean categoryExistsById(Long id);

}
