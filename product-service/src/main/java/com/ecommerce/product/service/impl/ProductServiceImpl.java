package com.ecommerce.product.service.impl;

import com.ecommerce.product.dto.ProductDto;
import com.ecommerce.product.entity.Category;
import com.ecommerce.product.entity.Product;
import com.ecommerce.product.exception.ResourceNotFoundException;
import com.ecommerce.product.mapper.ProductMapper;
import com.ecommerce.product.repository.CategoryRepository;
import com.ecommerce.product.repository.ProductRepository;
import com.ecommerce.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public List<ProductDto> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return ProductMapper.INSTANCE.productsToProductDtos(products);
    }

    @Override
    public ProductDto getProductById(Long id) {
        Product product = productRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Product", "id", id.toString())
        );

        return ProductMapper.INSTANCE.productToProductDto(product);
    }

    @Override
    public ProductDto createProduct(ProductDto productDto) {
        Category category = categoryRepository.findById(productDto.getCategoryId()).orElseThrow(
                () -> new ResourceNotFoundException("Category", "id", productDto.getCategoryId().toString())
        );
        Product product = ProductMapper.INSTANCE.productDtoToProduct(productDto);
        product.setCategory(category);
        Product savedProduct = productRepository.save(product);
        return ProductMapper.INSTANCE.productToProductDto(savedProduct);
    }

    @Override
    public ProductDto updateProduct(Long id, ProductDto productDto) {
        getProductById(id);
        Product product = ProductMapper.INSTANCE.productDtoToProduct(productDto);
        product.setId(id);
        Product updatedProduct = productRepository.save(product);
        return ProductMapper.INSTANCE.productToProductDto(updatedProduct);
    }

    @Override
    public String deleteProduct(Long id) {
        getProductById(id);
        productRepository.deleteById(id);
        return "Product deleted successfully";
    }
}
