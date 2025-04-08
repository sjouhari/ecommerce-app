package com.ecommerce.product.controller;

import com.ecommerce.product.dto.ProductRequestDto;
import com.ecommerce.product.dto.ProductResponseDto;
import com.ecommerce.product.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@Validated
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public ResponseEntity<List<ProductResponseDto>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDto> getProductById(@PathVariable Long id, @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(productService.getProductById(id, token));
    }

    @PostMapping
    public ResponseEntity<ProductResponseDto> createProduct(@RequestBody @Valid ProductRequestDto productDto, @RequestHeader("Authorization") String token) {
        return new ResponseEntity<>(productService.createProduct(productDto, token), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDto> updateProduct(@PathVariable Long id, @RequestBody @Valid ProductRequestDto productDto) {
        return ResponseEntity.ok(productService.updateProduct(id, productDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
        return ResponseEntity.ok(productService.deleteProduct(id));
    }

    @GetMapping("/existsById/{id}")
    public ResponseEntity<Boolean> productExistsById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.productExistsById(id));
    }

}
