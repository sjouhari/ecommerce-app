package com.ecommerce.order.repository;

import com.ecommerce.order.entity.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {

    Optional<ShoppingCart> findByUserId(Long userId);

    ShoppingCart getByUserId(Long userId);

    void deleteByUserId(Long userId);

}
