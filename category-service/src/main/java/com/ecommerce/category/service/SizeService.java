package com.ecommerce.category.service;

import com.ecommerce.category.dto.SizeDto;
import com.ecommerce.category.dto.SizeResponseDto;

import java.util.List;

public interface SizeService {

    List<SizeResponseDto> getAllSizes();

    SizeResponseDto getSizeById(Long id);

    SizeResponseDto createSize(SizeDto sizeDto);

    SizeResponseDto updateSize(Long id, SizeDto sizeDto);

    void deleteSize(Long id);

    boolean existsByLibelleAndCategoryId(String libelle, Long categoryId);
}
