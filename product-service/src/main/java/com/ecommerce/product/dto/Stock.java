package com.ecommerce.product.dto;

import com.ecommerce.product.enums.ProductColor;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Stock {

    private Long id;

    @NotBlank(message = "Size is required")
    private String size;

    @Enumerated(EnumType.STRING)
    private ProductColor color;

    @NotNull(message = "Quantity is required")
    @Positive(message = "Quantity must be positive")
    private int quantity;

    @NotNull(message = "Price is required")
    @PositiveOrZero(message = "Price must be positive")
    private double price;

}
