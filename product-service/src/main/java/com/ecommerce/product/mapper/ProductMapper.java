package com.ecommerce.product.mapper;

import com.ecommerce.product.dto.MediaDto;
import com.ecommerce.product.dto.ProductDto;
import com.ecommerce.product.dto.TvaDto;
import com.ecommerce.product.entity.Media;
import com.ecommerce.product.entity.Product;
import com.ecommerce.product.entity.Tva;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface ProductMapper {

    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    Product productDtoToProduct(ProductDto productDto);

    ProductDto productToProductDto(Product product);

    List<ProductDto> productsToProductDtos(List<Product> products);

    List<MediaDto> mediasToMediaDtos(List<Media> medias);

    List<Media> mediaDtosToMedias(List<MediaDto> mediaDtos);

    Media mediaDtoToMedia(MediaDto mediaDto);

    MediaDto mediaToMediaDto(Media media);

    Tva tvaDtoToTva(TvaDto tvaDto);

    TvaDto tvaToTvaDto(Tva tva);

}
