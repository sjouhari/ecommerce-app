package com.ecommerce.category.controller;

import com.ecommerce.category.dto.CategoryRequestDto;
import com.ecommerce.category.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@Validated
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<CategoryRequestDto>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    @GetMapping("{id}")
    public ResponseEntity<CategoryRequestDto> getCategoryById(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.getCategoryById(id));
    }

    @PostMapping
    public ResponseEntity<CategoryRequestDto> createCategory(@RequestBody @Valid CategoryRequestDto categoryRequestDto) {
        return new ResponseEntity<>(categoryService.createCategory(categoryRequestDto), HttpStatus.CREATED);
    }

    @PutMapping("{id}")
    public ResponseEntity<CategoryRequestDto> updateCategory(@PathVariable Long id, @RequestBody @Valid CategoryRequestDto categoryRequestDto) {
        return ResponseEntity.ok(categoryService.updateCategory(id, categoryRequestDto));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/existsById/{id}")
    public ResponseEntity<Boolean> categoryExistsById(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.categoryExistsById(id));
    }

    @GetMapping("{categoryId}/subcategories")
    public ResponseEntity<List<Long>> getAllSubCategoriesIdsByCategoryId(@PathVariable Long categoryId) {
        return ResponseEntity.ok(categoryService.getAllSubCategoriesIdsByCategoryId(categoryId));
    }

}
