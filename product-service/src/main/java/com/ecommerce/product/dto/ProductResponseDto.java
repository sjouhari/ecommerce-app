package com.ecommerce.product.dto;

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
public class ProductResponseDto {

    private Long id;
    private String name;
    private String description;
    private String type;
    private Double price;
    private double reductionPercentage;
    private List<ProductColor> productColors;
    private ProductStatus status;
    private Long subCategoryId;
    private List<SizeResponseDto> sizes;
    private TvaDto tva;
    private List<MediaDto> medias;

}
