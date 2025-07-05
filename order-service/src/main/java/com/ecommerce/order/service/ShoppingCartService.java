package com.ecommerce.order.service;

import com.ecommerce.order.dto.OrderItemRequestDto;
import com.ecommerce.order.dto.SelectOrderItemDto;
import com.ecommerce.order.dto.ShoppingCartDto;
import com.ecommerce.order.dto.UpdateOrderItemQauntityDto;

public interface ShoppingCartService {

    ShoppingCartDto getShoppingCartByUserId(Long userId);

    ShoppingCartDto addItemToShoppingCart(Long userId, OrderItemRequestDto orderItemRequestDto);

    void clearUserCart(Long userId);

    OrderItemRequestDto updateItemQuantity(Long id, UpdateOrderItemQauntityDto updateOrderItemQauntityDto);

    OrderItemRequestDto selectOrderItem(Long id, SelectOrderItemDto selectOrderItemDto);

    void deleteItemFromShoppingCart(Long id);
}
