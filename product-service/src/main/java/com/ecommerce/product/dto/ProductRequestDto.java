package com.ecommerce.product.dto;

import com.ecommerce.product.enums.ProductStatus;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequestDto {

    private Long id;

    @NotBlank(message = "Name is required")
    private String name;

    private String description;

    private String type;

    @NotNull(message = "Reduction percentage is required")
    @Min(value = 0, message = "Reduction percentage must be between 0 and 100")
    @Max(value = 100, message = "Reduction percentage must be between 0 and 100")
    private double reductionPercentage;

    @NotNull(message = "Status is required")
    private ProductStatus status;

    @NotNull(message = "Sub category id is required")
    private Long subCategoryId;

    @NotNull(message = "Tva id is required")
    private TvaDto tva;

    @NotNull(message = "Medias is required")
    private List<MediaDto> medias;

    private List<Stock> stock;
}
