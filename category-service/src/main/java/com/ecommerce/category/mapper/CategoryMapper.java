package com.ecommerce.category.mapper;

import com.ecommerce.category.dto.CategoryDto;
import com.ecommerce.category.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface CategoryMapper extends SubCategoryMapper, SizeMapper {

    CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);

    Category categoryDtoToCategory(CategoryDto categoryDto);

    CategoryDto categoryToCategoryDto(Category category);

    List<CategoryDto> categoriesToCategoryDtos(List<Category> categories);

}
