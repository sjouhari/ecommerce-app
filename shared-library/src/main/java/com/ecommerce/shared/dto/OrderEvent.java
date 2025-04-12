package com.ecommerce.shared.dto;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class OrderEvent {
    private Long orderId;
    private UserEvent user;
    private String status;
}
