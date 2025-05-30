package com.ecommerce.category.mapper;

import com.ecommerce.category.dto.CategoryRequestDto;
import com.ecommerce.category.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface CategoryMapper extends SubCategoryMapper, SizeMapper {

    CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);

    Category categoryDtoToCategory(CategoryRequestDto categoryRequestDto);

    CategoryRequestDto categoryToCategoryDto(Category category);

    List<CategoryRequestDto> categoriesToCategoryDtos(List<Category> categories);

}
