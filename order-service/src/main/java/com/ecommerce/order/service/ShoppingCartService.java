package com.ecommerce.order.service;

import com.ecommerce.order.dto.OrderItemDto;
import com.ecommerce.order.dto.ShoppingCartDto;

public interface ShoppingCartService {

    ShoppingCartDto getShoppingCartByUserId(Long userId);

    ShoppingCartDto addItemToShoppingCart(Long userId, OrderItemDto orderItemDto);

    OrderItemDto updateItemInShoppingCart(Long id, OrderItemDto orderItemDto);

    void deleteItemFromShoppingCart(Long id);
}
