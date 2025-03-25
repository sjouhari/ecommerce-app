package com.ecommerce.order.mapper;

import com.ecommerce.order.dto.ModePaymentDto;
import com.ecommerce.order.entity.ModePayment;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface ModePaymentMapper {

    ModePaymentMapper INSTANCE = Mappers.getMapper(ModePaymentMapper.class);

    ModePaymentDto modePaymentToModePaymentDto(ModePayment modePayment);

    ModePayment modePaymentDtoToModePayment(ModePaymentDto modePaymentDto);

    List<ModePaymentDto> modePaymentsToModePaymentDtos(List<ModePayment> modePayments);

}
