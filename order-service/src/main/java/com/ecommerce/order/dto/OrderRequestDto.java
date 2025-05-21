package com.ecommerce.order.dto;

import com.ecommerce.order.enums.PaymentMethods;
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
    private Long deliveryAddressId;

    @NotNull(message = "Order items are required")
    private List<Long> orderItemsIds;

    @NotNull(message = "Payment Method is required")
    private PaymentMethods paymentMethod;

    private String chequeNumber;
    private String bankName;

}
