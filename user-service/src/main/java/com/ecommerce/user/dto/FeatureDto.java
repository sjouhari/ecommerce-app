package com.ecommerce.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FeatureDto {

    private Long id;

    @NotBlank(message = "Libelle is required")
    private String libelle;

    @NotBlank(message = "Resource name is required")
    private String resourceName;

    @NotBlank(message = "Action is required")
    private String action;

}
