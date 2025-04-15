package com.ecommerce.product.service;

import com.ecommerce.product.dto.ProductRequestDto;
import com.ecommerce.product.dto.ProductResponseDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {

    List<ProductResponseDto> getAllProducts();

    ProductResponseDto getProductById(Long id, String token);

    ProductResponseDto createProduct(String productJson, List<MultipartFile> images, String token);

    ProductResponseDto updateProduct(Long id, ProductRequestDto productDto);

    String deleteProduct(Long id);

    boolean productExistsById(Long id);

}
