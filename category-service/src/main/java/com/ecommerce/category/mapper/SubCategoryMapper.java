package com.ecommerce.category.mapper;

import com.ecommerce.category.dto.SubCategoryDto;
import com.ecommerce.category.entity.Category;
import com.ecommerce.category.entity.SubCategory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface SubCategoryMapper {

    SubCategoryMapper INSTANCE = Mappers.getMapper(SubCategoryMapper.class);

    SubCategory subCategoryDtoToSubCategory(SubCategoryDto subCategoryDto);

    @Mapping(target = "categoryId", source = "category.id")
    SubCategoryDto subCategoryToSubCategoryDto(SubCategory subCategory);

    List<SubCategoryDto> subCategoriesToSubCateoryDtos(List<SubCategory> subCategories);


}
