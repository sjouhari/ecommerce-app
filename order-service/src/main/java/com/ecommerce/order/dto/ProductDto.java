package com.ecommerce.order.dto;

import com.ecommerce.shared.dto.MediaDto;
import com.ecommerce.shared.dto.StoreDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {

    private Long id;
    private String name;
    private List<MediaDto> medias;
    private StoreDto store;

}
