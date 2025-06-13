package com.ecommerce.order.repository;

import com.ecommerce.order.dto.BestSellingProductProjection;
import com.ecommerce.order.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    @Query("""
    SELECT o.productId AS productId, SUM(o.quantity) AS totalSold
    FROM OrderItem o
    GROUP BY o.productId
    ORDER BY totalSold DESC
    """)
    List<BestSellingProductProjection> findBestSellingProducts();

    @Query("""
    SELECT oi.productId AS productId, SUM(oi.quantity) AS totalSold
    FROM OrderItem oi
    JOIN oi.order o ON o.storeId = :storeId
    GROUP BY oi.productId
    ORDER BY totalSold DESC
    """)
    List<BestSellingProductProjection> findBestSellingProductsByStoreId(Long storeId);

}
