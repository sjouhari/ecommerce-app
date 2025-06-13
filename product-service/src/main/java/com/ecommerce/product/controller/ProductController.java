package com.ecommerce.product.controller;

import com.ecommerce.product.dto.ProductResponseDto;
import com.ecommerce.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@Validated
public class ProductController {

    @Autowired
    private ProductService productService;

    @Value("${app.images.path}")
    private String imagesPath;

    @GetMapping
    public ResponseEntity<List<ProductResponseDto>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @GetMapping("/approved")
    public ResponseEntity<List<ProductResponseDto>> getAllProductsByApprovedStores() {
        return ResponseEntity.ok(productService.getAllProductsByApprovedStores());
    }

    @GetMapping("/new")
    public ResponseEntity<List<ProductResponseDto>> getAllNewProducts() {
        return ResponseEntity.ok(productService.getNewProducts());
    }

    @GetMapping("/store/{id}")
    public ResponseEntity<List<ProductResponseDto>> getProductsByStoreId(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductsByStoreId(id));
    }

    @GetMapping("/subcategory/{id}")
    public ResponseEntity<List<ProductResponseDto>> getProductsBySubCategoryId(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductsBySubCategoryId(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDto> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProductResponseDto> createProduct(
            @RequestPart("product") String productJson,
            @RequestPart("images") List<MultipartFile> images,
            @RequestHeader("Authorization") String token) {
        return new ResponseEntity<>(productService.createProduct(productJson, images, token), HttpStatus.CREATED);
    }

    @PutMapping(path = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProductResponseDto> updateProduct(
            @PathVariable Long id,
            @RequestPart("product") String productJson,
            @RequestPart(value = "newImages", required = false) List<MultipartFile> newImages,
            @RequestPart(value = "deletedImages", required = false) String deletedImages,
            @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(productService.updateProduct(id, productJson, newImages, deletedImages, token));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/existsById/{id}")
    public ResponseEntity<Boolean> productExistsById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.productExistsById(id));
    }

    @GetMapping("/images/{imageName:.+}")
    public ResponseEntity<Resource> getImage(@PathVariable String imageName) {
        try {
            Path imagePath = Paths.get(imagesPath).resolve(imageName).normalize();
            Resource resource = new UrlResource(imagePath.toUri());

            if (resource.exists()) {
                String contentType = Files.probeContentType(imagePath);
                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(contentType))
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }

        } catch (IOException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/approve/{id}")
    public ResponseEntity<Void> approveProduct(@PathVariable Long id) {
        productService.approveProduct(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/reject/{id}")
    public ResponseEntity<Void> rejectProduct(@PathVariable Long id) {
        productService.rejectProduct(id);
        return ResponseEntity.ok().build();
    }

}
