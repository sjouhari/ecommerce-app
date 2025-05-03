package com.ecommerce.category.controller;

import com.ecommerce.category.dto.SizeDto;
import com.ecommerce.category.service.SizeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sizes")
@Validated
public class SizeController {

    @Autowired
    private SizeService sizeService;

    @GetMapping
    public ResponseEntity<List<SizeDto>> getAllSizes() {
        return ResponseEntity.ok(sizeService.getAllSizes());
    }

    @GetMapping("{id}")
    public ResponseEntity<SizeDto> getSizeById(@PathVariable Long id) {
        return ResponseEntity.ok(sizeService.getSizeById(id));
    }

    @PostMapping
    public ResponseEntity<SizeDto> createSize(@RequestBody @Valid SizeDto sizeDto) {
        return new ResponseEntity<>(sizeService.createSize(sizeDto), HttpStatus.CREATED);
    }

    @PutMapping("{id}")
    public ResponseEntity<SizeDto> updateSize(@PathVariable Long id, @RequestBody @Valid SizeDto sizeDto) {
        return new ResponseEntity<>(sizeService.updateSize(id, sizeDto), HttpStatus.CREATED);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteSize(@PathVariable Long id) {
        sizeService.deleteSize(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/existsByLibelleAndSubCategoryId/{libelle}/{subCategoryId}")
    public boolean existsByIdAndSubCategoryId(@PathVariable String libelle, @PathVariable Long subCategoryId) {
        return sizeService.existsByLibelleAndSubCategoryId(libelle, subCategoryId);
    }

}
