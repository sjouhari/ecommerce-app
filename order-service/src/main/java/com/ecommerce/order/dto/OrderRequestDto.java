package com.ecommerce.order.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequestDto {

    @NotNull(message = "User id is required")
    private Long userId;

    @NotNull(message = "Delivery address is required")
    private AddressDto deliveryAddress;

    @NotBlank(message = "Mode payment is required")
    private String modePayment;

    @NotNull(message = "Order items is required")
    private List<OrderItemDto> orderItems;


}
