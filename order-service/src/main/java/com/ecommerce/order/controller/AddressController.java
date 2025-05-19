package com.ecommerce.order.controller;

import com.ecommerce.order.dto.AddressDto;
import com.ecommerce.order.service.AddressService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/addresses")
@Validated
public class AddressController {

    @Autowired
    private AddressService addressService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<AddressDto>> getAllAddressesByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(addressService.getAllAddressesByUserId(userId));
    }

    @GetMapping("{addressId}")
    public ResponseEntity<AddressDto> getAddress(@PathVariable Long addressId) {
        return ResponseEntity.ok(addressService.getAddress(addressId));
    }

    @PostMapping
    public ResponseEntity<AddressDto> createAddress(@RequestBody @Valid AddressDto addressDto) {
        return ResponseEntity.ok(addressService.createAddress(addressDto));
    }

    @PutMapping("{addressId}")
    public ResponseEntity<AddressDto> updateAddress(@PathVariable Long addressId, @RequestBody @Valid AddressDto addressDto) {
        return ResponseEntity.ok(addressService.updateAddress(addressId, addressDto));
    }

    @DeleteMapping("{addressId}")
    public ResponseEntity<Void> deleteAddress(@PathVariable Long addressId) {
        addressService.deleteAddress(addressId);
        return ResponseEntity.noContent().build();
    }

}
