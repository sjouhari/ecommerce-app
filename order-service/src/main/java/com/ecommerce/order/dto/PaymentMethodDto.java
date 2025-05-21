package com.ecommerce.order.dto;

import com.ecommerce.order.enums.PaymentMethodStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentMethodDto {
    private Long id;
    private PaymentMethodStatus status;
    private String type;
}
