package com.ecommerce.product.dto;

import com.ecommerce.product.enums.ProductStatus;
import com.ecommerce.shared.dto.InventoryDto;
import com.ecommerce.shared.dto.MediaDto;
import com.ecommerce.shared.dto.StoreDto;
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

    private double reductionPercentage;

    private ProductStatus status;

    private Long subCategoryId;

    private String categoryName;

    private String subCategoryName;

    private TvaDto tva;

    private List<MediaDto> medias;

    private List<InventoryDto> stock;

    private StoreDto store;

    private boolean approved;

    private boolean rejected;

}
