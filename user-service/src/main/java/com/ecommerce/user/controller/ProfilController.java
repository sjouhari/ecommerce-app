package com.ecommerce.user.controller;

import com.ecommerce.user.dto.ProfilDto;
import com.ecommerce.user.dto.ProfilFeaturesDto;
import com.ecommerce.user.service.ProfilService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/profils")
@Validated
public class ProfilController {

    @Autowired
    private ProfilService profilService;

    @GetMapping
    public ResponseEntity<List<ProfilDto>> getAllProfils() {
        return ResponseEntity.ok(profilService.getAllProfils());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProfilDto> getProfilById(@PathVariable Long id) {
        return ResponseEntity.ok(profilService.getProfilById(id));
    }

    @PostMapping
    public ResponseEntity<ProfilDto> createProfil(@RequestBody @Valid ProfilDto profilDto) {
        return new ResponseEntity<>(profilService.createProfil(profilDto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProfilDto> updateProfil(@PathVariable Long id, @RequestBody @Valid ProfilDto profilDto) {
        return ResponseEntity.ok(profilService.updateProfil(id, profilDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProfil(@PathVariable Long id) {
        return ResponseEntity.ok(profilService.deleteProfil(id));
    }

    @PostMapping("/add-features")
    public ResponseEntity<ProfilDto> addFeaturesToProfil(@RequestBody @Valid ProfilFeaturesDto profilFeaturesDto) {
        return new ResponseEntity<>(profilService.addFeaturesToProfil(profilFeaturesDto), HttpStatus.CREATED);
    }

    @PostMapping("/remove-features")
    public ResponseEntity<ProfilDto> removeFeaturesFromProfil(@RequestBody @Valid ProfilFeaturesDto profilFeaturesDto) {
        return new ResponseEntity<>(profilService.removeFeaturesFromProfil(profilFeaturesDto), HttpStatus.CREATED);
    }

}
