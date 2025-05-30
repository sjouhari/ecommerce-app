package com.ecommerce.category.service.impl;

import com.ecommerce.category.dto.SizeDto;
import com.ecommerce.category.dto.SizeResponseDto;
import com.ecommerce.category.entity.Category;
import com.ecommerce.category.entity.Size;
import com.ecommerce.category.entity.SubCategory;
import com.ecommerce.category.mapper.SizeMapper;
import com.ecommerce.category.repository.CategoryRepository;
import com.ecommerce.category.repository.SizeRepository;
import com.ecommerce.category.service.SizeService;
import com.ecommerce.shared.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SizeServiceImpl implements SizeService {

    @Autowired
    private SizeRepository sizeRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public List<SizeResponseDto> getAllSizes() {
        List<Size> sizes = sizeRepository.findAll();
        return SizeMapper.INSTANCE.sizesToSizeDtos(sizes);
    }

    @Override
    public SizeResponseDto getSizeById(Long id) {
        Size size = sizeRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Size", "id", id.toString())
        );
        return SizeMapper.INSTANCE.sizeToSizeDto(size);
    }

    @Override
    public SizeResponseDto createSize(SizeDto sizeDto) {
        Size size = SizeMapper.INSTANCE.sizeDtoToSize(sizeDto);
        Category category = categoryRepository.findById(sizeDto.getCategoryId()).orElseThrow(
                () -> new ResourceNotFoundException("Category", "id", sizeDto.getCategoryId().toString())
        );
        size.setCategory(category);
        Size savedSize = sizeRepository.save(size);
        return SizeMapper.INSTANCE.sizeToSizeDto(savedSize);
    }

    @Override
    public SizeResponseDto updateSize(Long id, SizeDto sizeDto) {
        getSizeById(id);
        Size size = SizeMapper.INSTANCE.sizeDtoToSize(sizeDto);
        Category category = categoryRepository.findById(sizeDto.getCategoryId()).orElseThrow(
                () -> new ResourceNotFoundException("SubCategory", "id", sizeDto.getCategoryId().toString())
        );
        size.setCategory(category);
        size.setId(id);
        Size updatedSize = sizeRepository.save(size);
        return SizeMapper.INSTANCE.sizeToSizeDto(updatedSize);
    }

    @Override
    public void deleteSize(Long id) {
        getSizeById(id);
        sizeRepository.deleteById(id);
    }

    @Override
    public boolean existsByLibelleAndCategoryId(String libelle, Long categoryId) {
        return sizeRepository.existsByLibelleAndCategoryId(libelle, categoryId);
    }
}
