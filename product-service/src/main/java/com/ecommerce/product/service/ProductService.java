package com.ecommerce.product.service;

import com.ecommerce.product.dto.ProductResponseDto;
import com.ecommerce.product.entity.Product;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {

    List<ProductResponseDto> getAllProducts();

    List<ProductResponseDto> getAllProductsByApprovedStores();

    List<ProductResponseDto> getProductsByStoreId(Long storeId);

    List<ProductResponseDto> getProductsByCategoryId(Long categoryId);

    List<ProductResponseDto> getNewProducts();

    ProductResponseDto getProductById(Long id);

    ProductResponseDto createProduct(String productJson, List<MultipartFile> images, String token);

    ProductResponseDto updateProduct(Long id, String productJson, List<MultipartFile> newImages, String deletedImages, String token);

    void deleteProduct(Long id);

    boolean productExistsById(Long id);

    void approveProduct(Long id);

    void rejectProduct(Long id);

}
