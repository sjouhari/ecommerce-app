package com.ecommerce.order.service;

import com.ecommerce.order.dto.OrderRequestDto;
import com.ecommerce.order.dto.OrderResponseDto;
import com.ecommerce.order.dto.UpdateOrderStatusRequestDto;

import java.util.List;

public interface OrderService {

    List<OrderResponseDto> getAllOrders();

    List<OrderResponseDto> getOrdersByUserId(Long userId);

    OrderResponseDto getOrderById(Long orderId);

    OrderResponseDto placeOrder(OrderRequestDto orderRequestDto, String token);

    OrderResponseDto updateOrderStatus(Long orderId, UpdateOrderStatusRequestDto updateOrderStatusRequestDto);

    String deleteOrder(Long orderId);

}
