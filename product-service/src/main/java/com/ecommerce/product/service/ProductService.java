package com.ecommerce.product.service;

import com.ecommerce.product.dto.ProductRequestDto;
import com.ecommerce.product.dto.ProductResponseDto;

import java.util.List;

public interface ProductService {

    List<ProductResponseDto> getAllProducts();

    ProductResponseDto getProductById(Long id);

    ProductResponseDto createProduct(ProductRequestDto productDto, String token);

    ProductResponseDto updateProduct(Long id, ProductRequestDto productDto);

    String deleteProduct(Long id);

    boolean productExistsById(Long id);

}
