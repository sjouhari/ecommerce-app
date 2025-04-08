package com.ecommerce.inventory.repository;

import com.ecommerce.inventory.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    Optional<Inventory> findByProductIdAndSizeAndColor(Long productId, String size, String color);

    List<Inventory> findAllByProductId(Long productId);

    void deleteByProductId(Long productId);

}
