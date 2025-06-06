package com.ecommerce.order.controller;

import com.ecommerce.order.dto.*;
import com.ecommerce.order.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@Validated
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping
    public ResponseEntity<List<OrderResponseDto>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @GetMapping("{id}")
    public ResponseEntity<OrderResponseDto> getOrderById(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderResponseDto>> getOrdersByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(orderService.getOrdersByUserId(userId));
    }

    @PostMapping
    public ResponseEntity<OrderResponseDto> placeOrder(@RequestBody @Valid OrderRequestDto orderRequestDto, @RequestHeader("Authorization") String token) {
        return new ResponseEntity<>(orderService.placeOrder(orderRequestDto, token), HttpStatus.CREATED);
    }

    @PutMapping("{id}")
    public ResponseEntity<OrderResponseDto> updateOrderStatus(@PathVariable Long id, @RequestBody @Valid UpdateOrderStatusRequestDto updateOrderStatusRequestDto, @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(orderService.updateOrderStatus(id, updateOrderStatusRequestDto, token));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteOrder(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.deleteOrder(id));
    }

    @GetMapping("/stats/best-selling-products")
    public ResponseEntity<List<BestSellingProductProjection>> getBestSellingProducts() {
        return ResponseEntity.ok(orderService.getBestSellingProducts());
    }

}
