package com.ecommerce.order.mapper;

import com.ecommerce.order.dto.AddressDto;
import com.ecommerce.order.entity.Address;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface AddressMapper {

    AddressMapper INSTANCE = Mappers.getMapper(AddressMapper.class);

    Address addressDtoToAddress(AddressDto addressDto);

    AddressDto addressToAddressDto(Address address);

    List<AddressDto> addressesToAddressDtos(List<Address> addresses);

}
