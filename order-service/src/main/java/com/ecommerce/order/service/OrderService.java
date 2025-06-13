package com.ecommerce.order.service;

import com.ecommerce.order.dto.*;

import java.util.List;

public interface OrderService {

    List<OrderResponseDto> getAllOrders(String token);

    List<OrderResponseDto> getOrdersByUserId(Long userId, String token);

    List<OrderResponseDto> getOrdersByStoreId(Long storeId, String token);

    OrderResponseDto getOrderById(Long orderId, String token);

    void placeOrder(OrderRequestDto orderRequestDto, String token);

    OrderResponseDto updateOrderStatus(Long orderId, UpdateOrderStatusRequestDto updateOrderStatusRequestDto, String token);

    void confirmOrderPayement(Long paymentMethodId);

    String deleteOrder(Long orderId);

    List<BestSellingProductDto> getBestSellingProducts();

}
