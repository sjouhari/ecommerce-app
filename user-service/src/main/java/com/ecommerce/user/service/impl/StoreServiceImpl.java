package com.ecommerce.user.service.impl;

import com.ecommerce.shared.exception.ResourceNotFoundException;
import com.ecommerce.shared.dto.StoreDto;
import com.ecommerce.user.entity.Store;
import com.ecommerce.user.entity.User;
import com.ecommerce.user.exception.UserHasAlreadyAStoreException;
import com.ecommerce.user.mapper.StoreMapper;
import com.ecommerce.user.repository.ProfilRepository;
import com.ecommerce.user.repository.StoreRepository;
import com.ecommerce.user.repository.UserRepository;
import com.ecommerce.user.service.StoreService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StoreServiceImpl implements StoreService {

    private final StoreRepository storeRepository;
    private final UserRepository userRepository;
    private final ProfilRepository profilRepository;

    public StoreServiceImpl(StoreRepository storeRepository, UserRepository userRepository, ProfilRepository profilRepository) {
        this.storeRepository = storeRepository;
        this.userRepository = userRepository;
        this.profilRepository = profilRepository;
    }

    @Override
    public List<StoreDto> getAllStores() {
        return StoreMapper.INSTANCE.storesToStoreDtos(storeRepository.findAll());
    }

    @Override
    public List<Long> getAllApprovedStoresIds() {
        return storeRepository.findAllApprovedStoresIds();
    }

    @Override
    public StoreDto getStore(Long id) {
        Store store = storeRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Store", "id", id.toString())
        );
        return StoreMapper.INSTANCE.storeToStoreDto(store);
    }

    @Override
    public StoreDto createStore(StoreDto storeDto) {
        Store store = StoreMapper.INSTANCE.storeDtoToStore(storeDto);
        User user = userRepository.findById(storeDto.getUserId()).orElseThrow(
                () -> new ResourceNotFoundException("User", "id", storeDto.getUserId().toString())
        );
        if(user.getStore() != null) {
            throw new UserHasAlreadyAStoreException("User already has a store");
        }

        store.setUser(user);
        user.setStore(store);
        user.getProfils().add(profilRepository.findByName("ROLE_SELLER").orElseThrow(
                () -> new ResourceNotFoundException("Profile", "name", "ROLE_SELLER")
        ));
        userRepository.save(user);

        return StoreMapper.INSTANCE.storeToStoreDto(user.getStore());
    }

    @Override
    public StoreDto updateStore(Long id, StoreDto storeDto) {
        if(!storeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Store", "id", id.toString());
        }
        Store store = StoreMapper.INSTANCE.storeDtoToStore(storeDto);
        store.setId(id);
        return StoreMapper.INSTANCE.storeToStoreDto(storeRepository.save(store));
    }

    @Override
    public void deleteStore(Long id) {
        if(!storeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Store", "id", id.toString());
        }
        storeRepository.deleteById(id);
    }

    @Override
    public StoreDto approveStore(Long id) {
        Store store = storeRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Store", "id", id.toString())
        );
        store.setApproved(true);
        store.setRejected(false);
        return StoreMapper.INSTANCE.storeToStoreDto(storeRepository.save(store));
    }

    @Override
    public StoreDto rejectStore(Long id) {
        Store store = storeRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Store", "id", id.toString())
        );
        store.setRejected(true);
        store.setApproved(false);
        return StoreMapper.INSTANCE.storeToStoreDto(storeRepository.save(store));
    }
}
