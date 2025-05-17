package com.ecommerce.order.mapper;

import com.ecommerce.order.dto.ShoppingCartDto;
import com.ecommerce.order.entity.ShoppingCart;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ShoppingCartMapper {
    ShoppingCartMapper INSTANCE = Mappers.getMapper(ShoppingCartMapper.class);

    ShoppingCart shoppingCartDtoToShoppingCart(ShoppingCartDto shoppingCartDto);

    ShoppingCartDto shoppingCartToShoppingCartDto(ShoppingCart shoppingCart);
}
