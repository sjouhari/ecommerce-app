package com.ecommerce.order.dto;

import com.ecommerce.shared.enums.ProductColor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDto {
    private Long id;

    @NotNull(message = "Product id is required")
    private Long productId;

    private String productName;

    private String productImage;

    @NotBlank(message = "Size is required")
    private String size;

    @NotNull(message = "Color is required")
    private ProductColor color;

    @NotNull(message = "Quantity is required")
    @Positive(message = "Quantity must be greater than 0")
    private int quantity;

    @NotNull(message = "Price is required")
    private double price;

    private boolean selected = true;

}
