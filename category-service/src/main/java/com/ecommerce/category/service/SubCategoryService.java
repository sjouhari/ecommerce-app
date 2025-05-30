package com.ecommerce.category.service;

import com.ecommerce.category.dto.SubCategoryDto;
import com.ecommerce.category.dto.SubCategoryResponseDto;

import java.util.List;

public interface SubCategoryService {

    List<SubCategoryResponseDto> getAllSubCategories();

    SubCategoryResponseDto getSubCategoryById(Long id);

    SubCategoryResponseDto createSubCategory(SubCategoryDto subCategoryDto);

    SubCategoryResponseDto updateSubCategory(Long id, SubCategoryDto subCategoryDto);

    void deleteSubCategory(Long id);

    boolean existsById(Long id);

}
