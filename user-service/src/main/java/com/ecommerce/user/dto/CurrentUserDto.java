package com.ecommerce.user.dto;

import com.ecommerce.shared.dto.StoreDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CurrentUserDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private boolean enabled;
    private Set<ProfilDto> profils;
    private StoreDto store;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean subscribed;
    private String subscriptionEmail;
}
