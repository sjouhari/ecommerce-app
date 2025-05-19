package com.ecommerce.order.service;

import com.ecommerce.order.dto.AddressDto;

import java.util.List;

public interface AddressService {

    List<AddressDto> getAllAddressesByUserId(Long userId);

    AddressDto getAddress(Long addressId);

    AddressDto createAddress(AddressDto addressDto);

    AddressDto updateAddress(Long addressId, AddressDto addressDto);

    void deleteAddress(Long addressId);

}
