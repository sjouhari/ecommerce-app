package com.ecommerce.category.mapper;

import com.ecommerce.category.dto.SizeDto;
import com.ecommerce.category.entity.Size;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface SizeMapper {

    SizeMapper INSTANCE = Mappers.getMapper(SizeMapper.class);

    Size sizeDtoToSize(SizeDto sizeDto);

    @Mapping(target = "subCategoryId", source = "subCategory.id")
    SizeDto sizeToSizeDto(Size size);

    List<SizeDto> sizesToSizeDtos(List<Size> sizes);

}
