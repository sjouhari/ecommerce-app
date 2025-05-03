package com.ecommerce.category.service;

import com.ecommerce.category.dto.SubCategoryDto;

import java.util.List;

public interface SubCategoryService {

    List<SubCategoryDto> getAllSubCategories();

    SubCategoryDto getSubCategoryById(Long id);

    SubCategoryDto createSubCategory(SubCategoryDto subCategoryDto);

    SubCategoryDto updateSubCategory(Long id, SubCategoryDto subCategoryDto);

    void deleteSubCategory(Long id);

    boolean existsById(Long id);

}
