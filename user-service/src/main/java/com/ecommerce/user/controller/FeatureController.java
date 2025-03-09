package com.ecommerce.user.controller;

import com.ecommerce.user.dto.FeatureDto;
import com.ecommerce.user.service.FeatureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/features")
public class FeatureController {

    @Autowired
    private FeatureService featureService;

    @GetMapping
    public ResponseEntity<List<FeatureDto>> getAllFeatures() {
        return ResponseEntity.ok(featureService.getAllFeatures());
    }

    @GetMapping("/{id}") // http://localhost:8080/api/features/1
    public ResponseEntity<FeatureDto> getFeatureById(@PathVariable Long id) {
        return ResponseEntity.ok(featureService.getFeatureById(id));
    }

    @PostMapping
    public ResponseEntity<FeatureDto> createFeature(@RequestBody FeatureDto featureDto) {
        return new ResponseEntity<>(featureService.createFeature(featureDto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}") // http://localhost:8080/api/features/1
    public ResponseEntity<FeatureDto> updateFeature(@PathVariable Long id, @RequestBody FeatureDto featureDto) {
        return ResponseEntity.ok(featureService.updateFeature(id, featureDto));
    }


    @DeleteMapping("/{id}") // http://localhost:8080/api/features/1
    public ResponseEntity<String> deleteFeature(@PathVariable Long id) {
        return new ResponseEntity<>(featureService.deleteFeature(id), HttpStatus.OK);
    }

}
