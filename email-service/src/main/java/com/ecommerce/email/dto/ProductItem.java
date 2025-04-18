package com.ecommerce.email.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductItem {
    private String name;
    private double price;
    private int quantity;
    private String imageUrl;
}
