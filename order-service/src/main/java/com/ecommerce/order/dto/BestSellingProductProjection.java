package com.ecommerce.order.dto;

public interface BestSellingProductProjection {

    Long getProductId();

    String getProductName();

    String getProductImage();

    Long getTotalSold();

}
