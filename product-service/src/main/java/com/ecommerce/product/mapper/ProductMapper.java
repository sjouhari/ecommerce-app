package com.ecommerce.product.mapper;

import com.ecommerce.product.dto.InventoryDto;
import com.ecommerce.product.dto.*;
import com.ecommerce.product.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface ProductMapper {

    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    Product productRequestDtoToProduct(ProductRequestDto productDto);

    ProductResponseDto productToProductResponseDto(Product product);

    List<ProductResponseDto> productsToProductResponseDtos(List<Product> products);

    StockDto inventoryDtoToStockDto(InventoryDto inventoryDto);

    List<StockDto> inventoryDtosToStockDtos(List<InventoryDto> inventoryDtos);

}
