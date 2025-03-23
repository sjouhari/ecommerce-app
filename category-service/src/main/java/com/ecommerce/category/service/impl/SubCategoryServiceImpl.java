package com.ecommerce.category.service.impl;

import com.ecommerce.category.dto.SubCategoryDto;
import com.ecommerce.category.entity.Category;
import com.ecommerce.category.entity.SubCategory;
import com.ecommerce.category.exception.ResourceNotFoundException;
import com.ecommerce.category.mapper.SubCategoryMapper;
import com.ecommerce.category.repository.CategoryRepository;
import com.ecommerce.category.repository.SubCategoryRepository;
import com.ecommerce.category.service.SubCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubCategoryServiceImpl implements SubCategoryService {

    @Autowired
    private SubCategoryRepository subCategoryRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public List<SubCategoryDto> getAllSubCategories() {
        List<SubCategory> categories = subCategoryRepository.findAll();
        return SubCategoryMapper.INSTANCE.subCategoriesToSubCateoryDtos(categories);
    }

    @Override
    public SubCategoryDto getSubCategoryById(Long id) {
        SubCategory subCategory = subCategoryRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("SubCategory", "id", id.toString())
        );
        return SubCategoryMapper.INSTANCE.subCategoryToSubCategoryDto(subCategory);
    }

    @Override
    public SubCategoryDto createSubCategory(SubCategoryDto subCategoryDto) {
        SubCategory subCategory = SubCategoryMapper.INSTANCE.subCategoryDtoToSubCategory(subCategoryDto);
        Category category = categoryRepository.findById(subCategoryDto.getCategoryId()).orElseThrow(
                () -> new ResourceNotFoundException("Category", "id", subCategoryDto.getCategoryId().toString())
        );
        subCategory.setCategory(category);
        SubCategory savedSubCategory = subCategoryRepository.save(subCategory);
        return SubCategoryMapper.INSTANCE.subCategoryToSubCategoryDto(savedSubCategory);
    }

    @Override
    public SubCategoryDto updateSubCategory(Long id, SubCategoryDto subCategoryDto) {
        getSubCategoryById(id);
        SubCategory subCategory = SubCategoryMapper.INSTANCE.subCategoryDtoToSubCategory(subCategoryDto);
        subCategory.setId(id);
        Category category = categoryRepository.findById(subCategoryDto.getCategoryId()).orElseThrow(
                () -> new ResourceNotFoundException("Category", "id", subCategoryDto.getCategoryId().toString())
        );
        subCategory.setCategory(category);
        SubCategory updatedSubCategory = subCategoryRepository.save(subCategory);
        return SubCategoryMapper.INSTANCE.subCategoryToSubCategoryDto(updatedSubCategory);
    }

    @Override
    public String deleteSubCategory(Long id) {
        getSubCategoryById(id);
        subCategoryRepository.deleteById(id);
        return "Sub Category deleted successfully";
    }

    @Override
    public boolean existsById(Long id) {
        return subCategoryRepository.existsById(id);
    }
}
