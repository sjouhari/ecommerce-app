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
    public ResponseEntity<List<OrderResponseDto>> getAllOrders(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(orderService.getAllOrders(token));
    }

    @GetMapping("{id}")
    public ResponseEntity<OrderResponseDto> getOrderById(@PathVariable Long id, @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(orderService.getOrderById(id, token));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderResponseDto>> getOrdersByUserId(@PathVariable Long userId, @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(orderService.getOrdersByUserId(userId, token));
    }

    @GetMapping("/store/{storeId}")
    public ResponseEntity<List<OrderResponseDto>> getOrdersByStoreId(@PathVariable Long storeId, @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(orderService.getOrdersByStoreId(storeId, token));
    }

    @PostMapping
    public ResponseEntity<Void> placeOrder(@RequestBody @Valid OrderRequestDto orderRequestDto, @RequestHeader("Authorization") String token) {
        orderService.placeOrder(orderRequestDto, token);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("{id}")
    public ResponseEntity<OrderResponseDto> updateOrderStatus(@PathVariable Long id, @RequestBody @Valid UpdateOrderStatusRequestDto updateOrderStatusRequestDto, @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(orderService.updateOrderStatus(id, updateOrderStatusRequestDto, token));
    }

    @PutMapping("/payment/{paymentMethodId}")
    public ResponseEntity<Void> confirmOrderPayement(@PathVariable Long paymentMethodId) {
        orderService.confirmOrderPayement(paymentMethodId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteOrder(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.deleteOrder(id));
    }

    @GetMapping("/stats/best-selling-products")
    public ResponseEntity<List<BestSellingProductDto>> getBestSellingProducts() {
        return ResponseEntity.ok(orderService.getBestSellingProducts());
    }

    @GetMapping("/stats/best-selling-products/{storeId}")
    public ResponseEntity<List<BestSellingProductDto>> getBestSellingProductsByStoreId(@PathVariable Long storeId) {
        return ResponseEntity.ok(orderService.getBestSellingProductsByStoreId(storeId));
    }

}
