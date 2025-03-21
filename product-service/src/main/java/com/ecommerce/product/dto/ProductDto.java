package com.ecommerce.product.dto;

import com.ecommerce.product.entity.Media;
import com.ecommerce.product.entity.Tva;
import com.ecommerce.product.enums.ProductColor;
import com.ecommerce.product.enums.ProductStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
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

    @NotBlank(message = "Name is required")
    private String name;

    private String description;

    private String type;

    @NotNull(message = "Quantity in stock is required")
    @Positive(message = "Quantity in stock must be positive")
    private Integer quantityInStock;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be positive")
    private Double price;

    @NotNull(message = "Reduction percentage is required")
    @Size(min = 0, max = 100, message = "Reduction percentage must be between 0 and 100")
    private double reductionPercentage;

    private List<ProductColor> productColors;

    @NotNull(message = "Status is required")
    private ProductStatus status;

    @NotNull(message = "Sub category id is required")
    private Long subCategoryId;

    private TvaDto tva;

    private List<MediaDto> medias;
}
