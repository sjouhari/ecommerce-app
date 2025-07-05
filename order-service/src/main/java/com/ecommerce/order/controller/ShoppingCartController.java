package com.ecommerce.order.controller;

import com.ecommerce.order.dto.OrderItemRequestDto;
import com.ecommerce.order.dto.SelectOrderItemDto;
import com.ecommerce.order.dto.ShoppingCartDto;
import com.ecommerce.order.dto.UpdateOrderItemQauntityDto;
import com.ecommerce.order.service.ShoppingCartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/shopping-cart")
public class ShoppingCartController {

    private final ShoppingCartService shoppingCartService;

    public ShoppingCartController(ShoppingCartService shoppingCartService) {
        this.shoppingCartService = shoppingCartService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ShoppingCartDto> getShoppingCartByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(shoppingCartService.getShoppingCartByUserId(userId));
    }

    @PostMapping("/{userId}")
    public ResponseEntity<ShoppingCartDto> addItemToShoppingCart(@PathVariable Long userId, @RequestBody OrderItemRequestDto orderItemRequestDto) {
        return ResponseEntity.ok(shoppingCartService.addItemToShoppingCart(userId, orderItemRequestDto));
    }

    @DeleteMapping("user/{userId}")
    public ResponseEntity<Void> clearUserCart(@PathVariable Long userId) {
        shoppingCartService.clearUserCart(userId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/quantity/{id}")
    public ResponseEntity<OrderItemRequestDto> updateItemQuantityInShoppingCart(@PathVariable Long id, @RequestBody UpdateOrderItemQauntityDto updateOrderItemQauntityDto) {
        return ResponseEntity.ok(shoppingCartService.updateItemQuantity(id, updateOrderItemQauntityDto));
    }

    @PutMapping("/select/{id}")
    public ResponseEntity<OrderItemRequestDto> selectItemInShoppingCart(@PathVariable Long id, @RequestBody SelectOrderItemDto selectOrderItemDto) {
        return ResponseEntity.ok(shoppingCartService.selectOrderItem(id, selectOrderItemDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItemFromShoppingCart(@PathVariable Long id) {
        shoppingCartService.deleteItemFromShoppingCart(id);
        return ResponseEntity.noContent().build();
    }

}
