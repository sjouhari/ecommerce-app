package com.ecommerce.order.service;

import com.ecommerce.order.dto.*;

import java.util.List;

public interface OrderService {

    List<OrderResponseDto> getAllOrders();

    List<OrderResponseDto> getOrdersByUserId(Long userId);

    OrderResponseDto getOrderById(Long orderId);

    OrderResponseDto placeOrder(OrderRequestDto orderRequestDto, String token);

    OrderResponseDto updateOrderStatus(Long orderId, UpdateOrderStatusRequestDto updateOrderStatusRequestDto, String token);

    void confirmOrderPayement(Long paymentMethodId);

    String deleteOrder(Long orderId);

    List<BestSellingProductProjection> getBestSellingProducts();

}
