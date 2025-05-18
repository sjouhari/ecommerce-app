package com.ecommerce.order.entity;

import com.ecommerce.shared.enums.ProductColor;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "order_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Order order;

    @ManyToOne
    private ShoppingCart shoppingCart;

    private Long productId;

    private String productName;

    private String productImage;

    private String size;

    @Enumerated(EnumType.STRING)
    private ProductColor color;

    private int quantity;

    private double price;

}
