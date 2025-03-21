package com.ecommerce.category.service.impl;

import com.ecommerce.category.dto.SizeDto;
import com.ecommerce.category.entity.Size;
import com.ecommerce.category.entity.SubCategory;
import com.ecommerce.category.exception.ResourceNotFoundException;
import com.ecommerce.category.mapper.SizeMapper;
import com.ecommerce.category.repository.SizeRepository;
import com.ecommerce.category.repository.SubCategoryRepository;
import com.ecommerce.category.service.SizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SizeServiceImpl implements SizeService {

    @Autowired
    private SizeRepository sizeRepository;

    @Autowired
    private SubCategoryRepository subCategoryRepository;

    @Override
    public List<SizeDto> getAllSizes() {
        List<Size> sizes = sizeRepository.findAll();
        return SizeMapper.INSTANCE.sizesToSizeDtos(sizes);
    }

    @Override
    public SizeDto getSizeById(Long id) {
        Size size = sizeRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Size", "id", id.toString())
        );
        return SizeMapper.INSTANCE.sizeToSizeDto(size);
    }

    @Override
    public SizeDto createSize(SizeDto sizeDto) {
        Size size = SizeMapper.INSTANCE.sizeDtoToSize(sizeDto);
        SubCategory subCategory = subCategoryRepository.findById(sizeDto.getSubCategoryId()).orElseThrow(
                () -> new ResourceNotFoundException("SubCategory", "id", sizeDto.getSubCategoryId().toString())
        );
        size.setSubCategory(subCategory);
        Size savedSize = sizeRepository.save(size);
        return SizeMapper.INSTANCE.sizeToSizeDto(savedSize);
    }

    @Override
    public SizeDto updateSize(Long id, SizeDto sizeDto) {
        getSizeById(id);
        Size size = SizeMapper.INSTANCE.sizeDtoToSize(sizeDto);
        SubCategory subCategory = subCategoryRepository.findById(sizeDto.getSubCategoryId()).orElseThrow(
                () -> new ResourceNotFoundException("SubCategory", "id", sizeDto.getSubCategoryId().toString())
        );
        size.setSubCategory(subCategory);
        size.setId(id);
        Size updatedSize = sizeRepository.save(size);
        return SizeMapper.INSTANCE.sizeToSizeDto(updatedSize);
    }

    @Override
    public String deleteSize(Long id) {
        getSizeById(id);
        sizeRepository.deleteById(id);
        return "Size deleted successfully";
    }
}
