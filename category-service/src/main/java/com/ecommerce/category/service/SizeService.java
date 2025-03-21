package com.ecommerce.category.service;

import com.ecommerce.category.dto.SizeDto;

import java.util.List;

public interface SizeService {

    List<SizeDto> getAllSizes();

    SizeDto getSizeById(Long id);

    SizeDto createSize(SizeDto sizeDto);

    SizeDto updateSize(Long id, SizeDto sizeDto);

    String deleteSize(Long id);
}
