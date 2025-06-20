package com.ecommerce.order.dto;

import com.ecommerce.order.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponseDto {
    private Long id;
    private Long userId;
    private String userName;
    private Long storeId;
    private String storeName;
    private OrderStatus status;
    private List<OrderItemRequestDto> orderItems;
    private InvoiceDto invoice;
    private AddressDto deliveryAddress;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
