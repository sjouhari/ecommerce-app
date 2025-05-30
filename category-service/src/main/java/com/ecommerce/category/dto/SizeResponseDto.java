package com.ecommerce.category.dto;

import com.ecommerce.shared.dto.CategoryDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SizeResponseDto {

    private Long id;
    private String libelle;
    private CategoryDto category;

}
