package com.ecommerce.inventory.entity;

import com.ecommerce.shared.enums.ProductColor;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "inventories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long productId;
    private String size;

    @Enumerated(EnumType.STRING)
    private ProductColor color;

    private int quantity;
    private double price;

}
