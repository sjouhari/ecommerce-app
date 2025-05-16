package com.ecommerce.product.service;

import com.ecommerce.product.dto.ProductRequestDto;
import com.ecommerce.product.dto.ProductResponseDto;
import com.ecommerce.product.enums.ProductStatus;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {

    List<ProductResponseDto> getAllProducts(String token);

//    List<ProductResponseDto> getProductsBySellerId(Long sellerId);
//
//    List<ProductResponseDto> getProductsBySubCategoryId(Long subCategoryId);
//
//    List<ProductResponseDto> getProductsByStatus(ProductStatus status);

    List<ProductResponseDto> getNewProducts(String token);

    ProductResponseDto getProductById(Long id, String token);

    ProductResponseDto createProduct(String productJson, List<MultipartFile> images, String token);

    ProductResponseDto updateProduct(Long id, ProductRequestDto productDto);

    void deleteProduct(Long id);

    boolean productExistsById(Long id);

}
