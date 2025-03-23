package com.ecommerce.inventory.mapper;

import com.ecommerce.inventory.dto.InventoryDto;
import com.ecommerce.inventory.entity.Inventory;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface InventoryMapper {

    InventoryMapper INSTANCE = Mappers.getMapper(InventoryMapper.class);

    InventoryDto inventoryToInventoryDto(Inventory inventory);

    Inventory inventoryDtoToInventory(InventoryDto inventoryDto);

}
