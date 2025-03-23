package com.ecommerce.inventory.repository;

import com.ecommerce.inventory.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    Optional<Inventory> findByProductIdAndSizeId(Long productId, Long sizeId);

    void deleteByProductId(Long productId);

}
