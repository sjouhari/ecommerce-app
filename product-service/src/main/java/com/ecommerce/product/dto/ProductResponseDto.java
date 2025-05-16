package com.ecommerce.product.dto;

import com.ecommerce.product.enums.ProductStatus;
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
    private double reductionPercentage;
    private ProductStatus status;
    private Long subCategoryId;
    private String categoryName;
    private String subCategoryName;
    private Long sellerId;
    private TvaDto tva;
    private List<MediaDto> medias;
    private List<StockDto> stock;
}
