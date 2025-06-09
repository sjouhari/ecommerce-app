package com.ecommerce.user.controller;

import com.ecommerce.shared.dto.StoreDto;
import com.ecommerce.user.service.StoreService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/stores")
public class StoreController {

    private final StoreService storeService;

    public StoreController(StoreService storeService) {
        this.storeService = storeService;
    }

    @GetMapping("{id}")
    public ResponseEntity<StoreDto> getStoreById(@PathVariable Long id) {
        return ResponseEntity.ok(storeService.getStore(id));
    }

    @PostMapping
    public ResponseEntity<StoreDto> createStore(@RequestBody StoreDto storeDto) {
        return ResponseEntity.ok(storeService.createStore(storeDto));
    }

    @PutMapping("{id}")
    public ResponseEntity<StoreDto> updateStore(@PathVariable Long id, @RequestBody StoreDto storeDto) {
        return ResponseEntity.ok(storeService.updateStore(id, storeDto));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteStore(@PathVariable Long id) {
        storeService.deleteStore(id);
        return ResponseEntity.ok().build();
    }
}
