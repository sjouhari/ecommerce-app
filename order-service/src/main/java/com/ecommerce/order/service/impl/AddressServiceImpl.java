package com.ecommerce.order.service.impl;

import com.ecommerce.order.dto.AddressDto;
import com.ecommerce.order.entity.Address;
import com.ecommerce.order.mapper.AddressMapper;
import com.ecommerce.order.repository.AddressRepository;
import com.ecommerce.order.service.AddressService;
import com.ecommerce.shared.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    private AddressRepository addressRepository;

    @Override
    public List<AddressDto> getAllAddressesByUserId(Long userId) {
        List<Address> addresses = addressRepository.findAllByUserId(userId);
        return AddressMapper.INSTANCE.addressesToAddressDtos(addresses);
    }

    @Override
    public AddressDto getAddress(Long addressId) {
        Address address = addressRepository.findById(addressId).orElseThrow(
                () -> new ResourceNotFoundException("Address", "id", addressId.toString())
        );
        return AddressMapper.INSTANCE.addressToAddressDto(address);
    }

    @Override
    public AddressDto createAddress(AddressDto addressDto) {
        Address address = AddressMapper.INSTANCE.addressDtoToAddress(addressDto);
        Address savedAddress = addressRepository.save(address);
        return AddressMapper.INSTANCE.addressToAddressDto(savedAddress);
    }

    @Override
    public AddressDto updateAddress(Long addressId, AddressDto addressDto) {
        addressRepository.findById(addressId).orElseThrow(
                () -> new ResourceNotFoundException("Address", "id", addressId.toString())
        );
        Address address = AddressMapper.INSTANCE.addressDtoToAddress(addressDto);
        address.setId(addressId);
        Address savedAddress = addressRepository.save(address);
        return AddressMapper.INSTANCE.addressToAddressDto(savedAddress);
    }

    @Override
    public void deleteAddress(Long addressId) {
        addressRepository.findById(addressId).orElseThrow(
                () -> new ResourceNotFoundException("Address", "id", addressId.toString())
        );
        addressRepository.deleteById(addressId);
    }

}
