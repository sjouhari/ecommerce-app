package com.ecommerce.category.controller;

import com.ecommerce.category.dto.SizeDto;
import com.ecommerce.category.dto.SizeResponseDto;
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
    public ResponseEntity<List<SizeResponseDto>> getAllSizes() {
        return ResponseEntity.ok(sizeService.getAllSizes());
    }

    @GetMapping("{id}")
    public ResponseEntity<SizeResponseDto> getSizeById(@PathVariable Long id) {
        return ResponseEntity.ok(sizeService.getSizeById(id));
    }

    @PostMapping
    public ResponseEntity<SizeResponseDto> createSize(@RequestBody @Valid SizeDto sizeDto) {
        return new ResponseEntity<>(sizeService.createSize(sizeDto), HttpStatus.CREATED);
    }

    @PutMapping("{id}")
    public ResponseEntity<SizeResponseDto> updateSize(@PathVariable Long id, @RequestBody @Valid SizeDto sizeDto) {
        return new ResponseEntity<>(sizeService.updateSize(id, sizeDto), HttpStatus.CREATED);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteSize(@PathVariable Long id) {
        sizeService.deleteSize(id);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("existsByLibelleAndCategoryId/{libelle}/{categoryId}")
    public boolean existsByIdAndCategoryId(@PathVariable String libelle, @PathVariable Long categoryId) {
        return sizeService.existsByLibelleAndCategoryId(libelle, categoryId);
    }

}
