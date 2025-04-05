package com.ecommerce.email.dto;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewsletterDto {
    private String email;
    List<ProductItem> products;


}
