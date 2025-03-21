package com.ecommerce.category.controller;

import com.ecommerce.category.dto.SubCategoryDto;
import com.ecommerce.category.service.SubCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subcategories")
public class SubCategoryController {

    @Autowired
    private SubCategoryService subCategoryService;

    @GetMapping
    public ResponseEntity<List<SubCategoryDto>> getAllSubCategories() {
        return ResponseEntity.ok(subCategoryService.getAllSubCategories());
    }

    @GetMapping("{id}")
    public ResponseEntity<SubCategoryDto> getSubCategoryById(@PathVariable Long id) {
        return ResponseEntity.ok(subCategoryService.getSubCategoryById(id));
    }

    @PostMapping
    public ResponseEntity<SubCategoryDto> createSubCategoryById(@RequestBody SubCategoryDto subCategoryDto) {
        return ResponseEntity.ok(subCategoryService.createSubCategory(subCategoryDto));
    }

    @PutMapping("{id}")
    public ResponseEntity<SubCategoryDto> updateSubCategoryById(@PathVariable Long id, @RequestBody SubCategoryDto subCategoryDto) {
        return ResponseEntity.ok(subCategoryService.updateSubCategory(id, subCategoryDto));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteSubCategoryById(@PathVariable Long id) {
        return ResponseEntity.ok(subCategoryService.deleteSubCategory(id));
    }


}
