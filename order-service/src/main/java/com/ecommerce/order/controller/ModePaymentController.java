package com.ecommerce.order.controller;

import com.ecommerce.order.dto.ModePaymentDto;
import com.ecommerce.order.service.ModePaymentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mode-payment")
@Validated
public class ModePaymentController {

    @Autowired
    private ModePaymentService modePaymentService;

    @GetMapping
    public ResponseEntity<List<ModePaymentDto>> getAllModePayments() {
        return ResponseEntity.ok(modePaymentService.getAllModePayments());
    }

    @GetMapping("{id}")
    public ResponseEntity<ModePaymentDto> getModePaymentById(@PathVariable Long id) {
        return ResponseEntity.ok(modePaymentService.getModePaymentById(id));
    }

    @PostMapping
    public ResponseEntity<ModePaymentDto> createModePayment(@RequestBody @Valid ModePaymentDto modePaymentDto) {
        return new ResponseEntity<>(modePaymentService.createModePayment(modePaymentDto), HttpStatus.CREATED);
    }

    @PutMapping("{id}")
    public ResponseEntity<ModePaymentDto> updateModePayment(@PathVariable Long id, @RequestBody @Valid ModePaymentDto modePaymentDto) {
        return ResponseEntity.ok(modePaymentService.updateModePayment(id, modePaymentDto));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteModePayment(@PathVariable Long id) {
        return ResponseEntity.ok(modePaymentService.deleteModePayment(id));
    }
}
