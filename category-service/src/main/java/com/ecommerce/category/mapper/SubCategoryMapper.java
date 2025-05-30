package com.ecommerce.category.mapper;

import com.ecommerce.category.dto.SubCategoryDto;
import com.ecommerce.category.dto.SubCategoryResponseDto;
import com.ecommerce.category.entity.SubCategory;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface SubCategoryMapper {

    SubCategoryMapper INSTANCE = Mappers.getMapper(SubCategoryMapper.class);

    SubCategory subCategoryDtoToSubCategory(SubCategoryDto subCategoryDto);

    SubCategoryResponseDto subCategoryToSubCategoryDto(SubCategory subCategory);

    List<SubCategoryResponseDto> subCategoriesToSubCateoryDtos(List<SubCategory> subCategories);


}
