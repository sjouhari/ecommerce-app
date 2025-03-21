package com.ecommerce.category.controller;

import com.ecommerce.category.dto.SizeDto;
import com.ecommerce.category.service.SizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sizes")
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
    public ResponseEntity<SizeDto> createSize(@RequestBody SizeDto sizeDto) {
        return new ResponseEntity<>(sizeService.createSize(sizeDto), HttpStatus.CREATED);
    }

    @PutMapping("{id}")
    public ResponseEntity<SizeDto> updateSize(@PathVariable Long id, @RequestBody SizeDto sizeDto) {
        return new ResponseEntity<>(sizeService.updateSize(id, sizeDto), HttpStatus.CREATED);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteSize(@PathVariable Long id) {
        return ResponseEntity.ok(sizeService.deleteSize(id));
    }

}
