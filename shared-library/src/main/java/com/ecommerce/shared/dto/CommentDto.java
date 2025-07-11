package com.ecommerce.shared.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {

    private Long id;

    @NotBlank(message = "Content is required")
    private String content;

    private LocalDateTime createdAt;

    private double rating;

    @NotNull(message = "Product id is required")
    private Long productId;

    @NotNull(message = "User id is required")
    private Long userId;

    private String username;

    private boolean approved;

    private boolean rejected;

}
