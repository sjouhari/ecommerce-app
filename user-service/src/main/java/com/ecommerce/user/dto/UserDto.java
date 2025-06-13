package com.ecommerce.user.dto;


import com.ecommerce.shared.dto.StoreDto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private Long id;

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    private boolean enabled;

    @NotNull(message = "Profils are required")
    private List<ProfilDto> profils;

    private StoreDto store;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private boolean subscribed;

    private String subscriptionEmail;

}
