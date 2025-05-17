package com.ecommerce.order.controller;

import com.ecommerce.order.dto.OrderItemDto;
import com.ecommerce.order.dto.ShoppingCartDto;
import com.ecommerce.order.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/shopping-cart")
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    @GetMapping("/{userId}")
    public ResponseEntity<ShoppingCartDto> getShoppingCartByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(shoppingCartService.getShoppingCartByUserId(userId));
    }

    @PostMapping("/{userId}")
    public ResponseEntity<ShoppingCartDto> addItemToShoppingCart(@PathVariable Long userId, @RequestBody OrderItemDto orderItemDto) {
        return ResponseEntity.ok(shoppingCartService.addItemToShoppingCart(userId, orderItemDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderItemDto> updateItemInShoppingCart(@PathVariable Long id, @RequestBody OrderItemDto orderItemDto) {
        return ResponseEntity.ok(shoppingCartService.updateItemInShoppingCart(id, orderItemDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItemFromShoppingCart(@PathVariable Long id) {
        shoppingCartService.deleteItemFromShoppingCart(id);
        return ResponseEntity.noContent().build();
    }

}
