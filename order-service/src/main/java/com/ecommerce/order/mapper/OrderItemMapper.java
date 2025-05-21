package com.ecommerce.order.mapper;

import com.ecommerce.order.dto.OrderItemDto;
import com.ecommerce.order.entity.OrderItem;
import com.ecommerce.shared.dto.InventoryDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface OrderItemMapper {

    OrderItemMapper INSTANCE = Mappers.getMapper(OrderItemMapper.class);

    OrderItemDto orderItemToOrderItemDto(OrderItem orderItem);

    OrderItem orderItemDtoToOrderItem(OrderItemDto orderItemDto);

    InventoryDto orderItemToInventoryDto(OrderItem orderItem);

}
