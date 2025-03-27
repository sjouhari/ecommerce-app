package com.ecommerce.user.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProfilFeaturesDto {

    @NotNull(message = "Profil id is required")
    private Long profilId;

    @NotNull(message = "Feature ids are required")
    private Set<Long> featureIds;
}
