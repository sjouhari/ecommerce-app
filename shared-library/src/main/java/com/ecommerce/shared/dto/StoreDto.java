package com.ecommerce.shared.dto;

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
public class StoreDto {

    private Long id;

    @NotBlank(message = "Store name is required")
    private String name;

    @NotBlank(message = "Store address is required")
    private String address;

    @NotBlank(message = "Store phone number is required")
    private String phoneNumber;

    @NotBlank(message = "Store email is required")
    private String email;

    @NotNull(message = "User id is required")
    private Long userId;

}
