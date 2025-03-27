package com.ecommerce.order.dto;

import com.ecommerce.order.enums.FactureStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FactureDto {

    private Long id;

    @NotBlank(message = "Order id is required")
    private String modePayment;

    private double totalPrice;

    @NotNull(message = "Status is required")
    private FactureStatus status;
}
