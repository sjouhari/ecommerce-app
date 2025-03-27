package com.ecommerce.product.controller;

import com.ecommerce.product.dto.TvaDto;
import com.ecommerce.product.service.TvaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tvas")
@Validated
public class TvaController {

    @Autowired
    private TvaService tvaService;

    @GetMapping
    public ResponseEntity<List<TvaDto>> getAllTvas() {
        return ResponseEntity.ok(tvaService.getAllTvas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TvaDto> getTvaById(@PathVariable Long id) {
        return ResponseEntity.ok(tvaService.getTvaById(id));
    }

    @PostMapping
    public ResponseEntity<TvaDto> createTva(@RequestBody @Valid TvaDto tvaDto) {
        return new ResponseEntity<>(tvaService.createTva(tvaDto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TvaDto> updateTva(@PathVariable Long id, @RequestBody @Valid TvaDto tvaDto) {
        return ResponseEntity.ok(tvaService.updateTva(id, tvaDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTva(@PathVariable Long id) {
        return ResponseEntity.ok(tvaService.deleteTva(id));
    }

}
