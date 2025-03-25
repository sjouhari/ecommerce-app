package com.ecommerce.order.dto;

import com.ecommerce.order.enums.FactureStatus;
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
    private String modePayment;
    private double totalPrice;
    private FactureStatus status;
}
