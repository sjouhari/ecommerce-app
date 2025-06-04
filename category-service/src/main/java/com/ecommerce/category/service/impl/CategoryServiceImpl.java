package com.ecommerce.category.service.impl;

import com.ecommerce.category.dto.CategoryRequestDto;
import com.ecommerce.category.entity.Category;
import com.ecommerce.category.mapper.CategoryMapper;
import com.ecommerce.category.repository.CategoryRepository;
import com.ecommerce.category.service.CategoryService;
import com.ecommerce.shared.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public List<CategoryRequestDto> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return CategoryMapper.INSTANCE.categoriesToCategoryDtos(categories);
    }

    @Override
    public CategoryRequestDto getCategoryById(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Category", "id", id.toString())
        );
        return CategoryMapper.INSTANCE.categoryToCategoryDto(category);
    }

    @Override
    public CategoryRequestDto createCategory(CategoryRequestDto categoryRequestDto) {
        Category category = CategoryMapper.INSTANCE.categoryDtoToCategory(categoryRequestDto);
        Category savedCategory = categoryRepository.save(category);
        return CategoryMapper.INSTANCE.categoryToCategoryDto(savedCategory);
    }

    @Override
    public CategoryRequestDto updateCategory(Long id, CategoryRequestDto categoryRequestDto) {
        getCategoryById(id);
        Category category = CategoryMapper.INSTANCE.categoryDtoToCategory(categoryRequestDto);
        category.setId(id);
        Category updatedCategory = categoryRepository.save(category);
        return CategoryMapper.INSTANCE.categoryToCategoryDto(updatedCategory);
    }

    @Override
    public void deleteCategory(Long id) {
        getCategoryById(id);
        categoryRepository.deleteById(id);
    }

    @Override
    public boolean categoryExistsById(Long id) {
        return categoryRepository.existsById(id);
    }
}
