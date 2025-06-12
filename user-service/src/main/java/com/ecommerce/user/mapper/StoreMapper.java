package com.ecommerce.user.mapper;

import com.ecommerce.shared.dto.StoreDto;
import com.ecommerce.user.entity.Store;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface StoreMapper {

    StoreMapper INSTANCE = Mappers.getMapper(StoreMapper.class);

    @Mapping(target = "userId", source = "user.id")
    StoreDto storeToStoreDto(Store store);

    Store storeDtoToStore(StoreDto storeDto);

    List<StoreDto> storesToStoreDtos(List<Store> stores);

}
