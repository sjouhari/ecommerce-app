package com.ecommerce.product.mapper;

import com.ecommerce.product.dto.*;
import com.ecommerce.product.entity.Media;
import com.ecommerce.product.entity.Product;
import com.ecommerce.product.entity.Tva;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface ProductMapper {

    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    Product productRequestDtoToProduct(ProductRequestDto productDto);

    ProductResponseDto productToProductResponseDto(Product product);

    List<ProductResponseDto> productsToProductResponseDtos(List<Product> products);

    List<MediaDto> mediasToMediaDtos(List<Media> medias);

    List<Media> mediaDtosToMedias(List<MediaDto> mediaDtos);

    Media mediaDtoToMedia(MediaDto mediaDto);

    MediaDto mediaToMediaDto(Media media);

    Tva tvaDtoToTva(TvaDto tvaDto);

    TvaDto tvaToTvaDto(Tva tva);

    @Mapping(target = "id", source = "inventoryDto.id")
    @Mapping(target = "sizeId", source = "inventoryDto.sizeId")
    @Mapping(target = "quantity", source = "inventoryDto.quantity")
    SizeResponseDto inventoryDtoToSizeResponseDto(InventoryDto inventoryDto);

    List<SizeResponseDto> inventoryDtosToSizeResponseDtos(List<InventoryDto> inventoryDtos);

}
