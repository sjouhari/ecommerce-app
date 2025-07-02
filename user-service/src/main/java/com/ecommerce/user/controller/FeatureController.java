package com.ecommerce.user.controller;

import com.ecommerce.user.dto.FeatureDto;
import com.ecommerce.user.service.FeatureService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/features")
@Validated
public class FeatureController {

    private final FeatureService featureService;

    public FeatureController(FeatureService featureService) {
        this.featureService = featureService;
    }

    @GetMapping
    public ResponseEntity<List<FeatureDto>> getAllFeatures() {
        return ResponseEntity.ok(featureService.getAllFeatures());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FeatureDto> getFeatureById(@PathVariable Long id) {
        return ResponseEntity.ok(featureService.getFeatureById(id));
    }

    @PostMapping
    public ResponseEntity<FeatureDto> createFeature(@RequestBody @Valid FeatureDto featureDto) {
        return new ResponseEntity<>(featureService.createFeature(featureDto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FeatureDto> updateFeature(@PathVariable Long id, @RequestBody @Valid FeatureDto featureDto) {
        return ResponseEntity.ok(featureService.updateFeature(id, featureDto));
    }


    @DeleteMapping("/{id}") // http://localhost:8080/api/features/1
    public ResponseEntity<Void> deleteFeature(@PathVariable Long id) {
        featureService.deleteFeature(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
