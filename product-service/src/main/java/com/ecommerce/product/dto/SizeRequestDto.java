package com.ecommerce.product.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SizeRequestDto {

    @NotNull(message = "Size id is required")
    private Long sizeId;

    @NotNull(message = "Quantity is required")
    private int quantity;

}
