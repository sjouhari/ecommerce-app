package com.ecommerce.order.repository;

import com.ecommerce.order.dto.BestSellingProductProjection;
import com.ecommerce.order.dto.TopSellersProjection;
import com.ecommerce.order.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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
    SELECT o.order.storeId AS storeId, SUM(o.quantity) AS total
    FROM OrderItem o
    WHERE o.order.storeId IS NOT NULL
    GROUP BY o.order.storeId
    ORDER BY SUM(o.quantity) DESC
    """)
    List<TopSellersProjection> findTopSellers();


    @Query("""
    SELECT oi.productId AS productId, SUM(oi.quantity) AS totalSold
    FROM OrderItem oi
    JOIN oi.order o
    WHERE o.storeId = :storeId
    GROUP BY oi.productId
    ORDER BY totalSold DESC
    """)
    List<BestSellingProductProjection> findBestSellingProductsByStoreId(@Param("storeId") Long storeId);


}
