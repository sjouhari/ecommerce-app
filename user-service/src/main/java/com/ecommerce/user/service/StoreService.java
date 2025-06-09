package com.ecommerce.user.service;

import com.ecommerce.shared.dto.StoreDto;

public interface StoreService {

    StoreDto getStore(Long id);

    StoreDto createStore(StoreDto storeDto);

    StoreDto updateStore(Long id, StoreDto storeDto);

    void deleteStore(Long id);

}
