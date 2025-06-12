package com.ecommerce.user.service;

import com.ecommerce.shared.dto.StoreDto;

import java.util.List;

public interface StoreService {

    List<StoreDto> getAllStores();

    List<Long> getAllApprovedStoresIds();

    StoreDto getStore(Long id);

    StoreDto createStore(StoreDto storeDto);

    StoreDto updateStore(Long id, StoreDto storeDto);

    void deleteStore(Long id);

    StoreDto approveStore(Long id);

    StoreDto rejectStore(Long id);

}
