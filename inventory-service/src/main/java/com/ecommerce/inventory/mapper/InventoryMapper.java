package com.ecommerce.inventory.mapper;

import com.ecommerce.inventory.entity.Inventory;
import com.ecommerce.shared.dto.InventoryDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface InventoryMapper {

    InventoryMapper INSTANCE = Mappers.getMapper(InventoryMapper.class);

    InventoryDto inventoryToInventoryDto(Inventory inventory);

    Inventory inventoryDtoToInventory(InventoryDto inventoryDto);

    List<InventoryDto> inventoryListToInventoryDtoList(List<Inventory> inventoryList);

}
