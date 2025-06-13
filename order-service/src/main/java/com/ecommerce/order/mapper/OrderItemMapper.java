package com.ecommerce.order.mapper;

import com.ecommerce.order.dto.OrderItemRequestDto;
import com.ecommerce.order.entity.OrderItem;
import com.ecommerce.shared.dto.InventoryDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface OrderItemMapper {

    OrderItemMapper INSTANCE = Mappers.getMapper(OrderItemMapper.class);

    OrderItemRequestDto orderItemToOrderItemDto(OrderItem orderItem);

    OrderItem orderItemDtoToOrderItem(OrderItemRequestDto orderItemRequestDto);

    InventoryDto orderItemToInventoryDto(OrderItem orderItem);

}
