package com.ecommerce.order.mapper;

import com.ecommerce.order.dto.FactureDto;
import com.ecommerce.order.dto.OrderRequestDto;
import com.ecommerce.order.dto.OrderResponseDto;
import com.ecommerce.order.entity.Facture;
import com.ecommerce.order.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface OrderMapper {

    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

    Order orderRequestDtoToOrder(OrderRequestDto orderRequestDto);

    OrderResponseDto orderToOrderResponseDto(Order order);

    List<OrderResponseDto> ordersToOrderResponseDtos(List<Order> orders);

    @Mapping(target = "modePayment", source = "facture.modePayment.name")
    FactureDto factureToFactureDto(Facture facture);

}
