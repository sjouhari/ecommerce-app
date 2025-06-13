package com.ecommerce.product.entity;

import com.ecommerce.product.enums.ProductStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Product extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    private String description;

    private double reductionPercentage;

    private Long storeId;

    private boolean approved = false;

    private boolean rejected = false;

    @Enumerated(EnumType.STRING)
    private ProductStatus status;

    private Long subCategoryId;

    @ManyToOne
    private Tva tva;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Media> medias;

}
