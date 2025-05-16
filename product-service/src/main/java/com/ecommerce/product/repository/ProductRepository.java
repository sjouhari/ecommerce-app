package com.ecommerce.product.repository;

import com.ecommerce.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    boolean existsByName(String name);

    @Query(
            value = "SELECT * FROM products WHERE created_at >= CURRENT_DATE - INTERVAL '30 days' ORDER BY created_at DESC",
            nativeQuery = true
    )
    List<Product> findAllNewProducts();

}
