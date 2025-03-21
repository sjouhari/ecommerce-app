package com.ecommerce.category.service.impl;

import com.ecommerce.category.dto.CategoryDto;
import com.ecommerce.category.entity.Category;
import com.ecommerce.category.exception.ResourceNotFoundException;
import com.ecommerce.category.mapper.CategoryMapper;
import com.ecommerce.category.repository.CategoryRepository;
import com.ecommerce.category.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public List<CategoryDto> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return CategoryMapper.INSTANCE.categoriesToCategoryDtos(categories);
    }

    @Override
    public CategoryDto getCategoryById(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Category", "id", id.toString())
        );
        return CategoryMapper.INSTANCE.categoryToCategoryDto(category);
    }

    @Override
    public CategoryDto createCategory(CategoryDto categoryDto) {
        Category category = CategoryMapper.INSTANCE.categoryDtoToCategory(categoryDto);
        Category savedCategory = categoryRepository.save(category);
        return CategoryMapper.INSTANCE.categoryToCategoryDto(savedCategory);
    }

    @Override
    public CategoryDto updateCategory(Long id, CategoryDto categoryDto) {
        getCategoryById(id);
        Category category = CategoryMapper.INSTANCE.categoryDtoToCategory(categoryDto);
        category.setId(id);
        Category updatedCategory = categoryRepository.save(category);
        return CategoryMapper.INSTANCE.categoryToCategoryDto(updatedCategory);
    }

    @Override
    public String deleteCategory(Long id) {
        getCategoryById(id);
        categoryRepository.deleteById(id);
        return "Category deleted successfully";
    }
}
