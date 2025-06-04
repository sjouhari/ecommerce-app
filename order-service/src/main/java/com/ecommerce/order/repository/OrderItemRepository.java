package com.ecommerce.order.repository;

import com.ecommerce.order.dto.BestSellingProductProjection;
import com.ecommerce.order.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    @Query("""
    SELECT o.productId AS productId, o.productName AS productName, o.productImage AS productImage, SUM(o.quantity) AS totalSold
    FROM OrderItem o
    GROUP BY o.productId, o.productName, o.productImage
    ORDER BY totalSold DESC
    """)
    List<BestSellingProductProjection> findBestSellingProducts();

}
