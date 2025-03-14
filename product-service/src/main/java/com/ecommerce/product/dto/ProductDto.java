package com.ecommerce.product.dto;

import com.ecommerce.product.enums.ProductColor;
import com.ecommerce.product.enums.ProductStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {

    private Long id;

    @NotBlank(message = "Product Name is required")
    private String name;

    @NotBlank(message = "Product Description is required")
    private String description;

    @NotNull(message = "Product Price is required")
    @Positive(message = "Product Price must be positive")
    private double price;

    @NotNull(message = "Product Quantity in Stock is required")
    @Positive(message = "Product Quantity in Stock must be positive")
    private int quantityInStock;

    @NotNull(message = "Product Colors is required")
    private List<ProductColor> colors;

    @NotNull(message = "Product Status is required")
    private ProductStatus status;

    @NotNull(message = "Product Category is required")
    private Long categoryId;

}
