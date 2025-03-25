package com.ecommerce.order.dto;

import com.ecommerce.order.enums.OrderStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateOrderStatusRequestDto {

    @NotNull(message = "Status is required")
    private OrderStatus status;

}
