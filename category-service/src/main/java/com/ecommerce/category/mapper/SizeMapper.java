package com.ecommerce.category.mapper;

import com.ecommerce.category.dto.SizeDto;
import com.ecommerce.category.dto.SizeResponseDto;
import com.ecommerce.category.entity.Size;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface SizeMapper {

    SizeMapper INSTANCE = Mappers.getMapper(SizeMapper.class);

    Size sizeDtoToSize(SizeDto sizeDto);

    SizeResponseDto sizeToSizeDto(Size size);

    List<SizeResponseDto> sizesToSizeDtos(List<Size> sizes);

}
