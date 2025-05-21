package com.ecommerce.order.mapper;

import com.ecommerce.order.dto.OrderResponseDto;
import com.ecommerce.order.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface OrderMapper extends PaymentMethodMapper {

    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

    OrderResponseDto orderToOrderResponseDto(Order order);

    List<OrderResponseDto> ordersToOrderResponseDtos(List<Order> orders);

}
