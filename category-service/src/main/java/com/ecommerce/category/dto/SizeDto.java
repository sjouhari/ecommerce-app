package com.ecommerce.category.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SizeDto {

    private Long id;

    @NotBlank(message = "Libelle is required")
    private String libelle;

    @NotNull(message = "Category id is required")
    private Long categoryId;

}
