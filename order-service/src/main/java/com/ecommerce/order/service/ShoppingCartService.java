package com.ecommerce.order.service;

import com.ecommerce.order.dto.OrderItemDto;
import com.ecommerce.order.dto.ShoppingCartDto;
import com.ecommerce.order.dto.UpdateOrderItemQauntityDto;

public interface ShoppingCartService {

    ShoppingCartDto getShoppingCartByUserId(Long userId);

    ShoppingCartDto addItemToShoppingCart(Long userId, OrderItemDto orderItemDto);

    OrderItemDto updateItemQuantity(Long id, UpdateOrderItemQauntityDto updateOrderItemQauntityDto);

    void deleteItemFromShoppingCart(Long id);
}
