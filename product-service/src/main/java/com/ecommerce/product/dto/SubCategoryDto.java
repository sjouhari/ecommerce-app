package com.ecommerce.product.dto;

import com.ecommerce.shared.dto.CategoryDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SubCategoryDto {
    private Long id;
    private String name;
    private CategoryDto category;
}
