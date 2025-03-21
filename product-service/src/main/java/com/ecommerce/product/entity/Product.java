package com.ecommerce.product.entity;

import com.ecommerce.product.enums.ProductColor;
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

    private String type;

    private Integer quantityInStock;

    private Double price;

    private double reductionPercentage;

    @ElementCollection
    @CollectionTable(name = "product_colors", joinColumns = @JoinColumn(name = "product_id"))
    @Enumerated(EnumType.STRING)
    private List<ProductColor> productColors;

    @Enumerated(EnumType.STRING)
    private ProductStatus status;

    private Long subCategoryId;

    @ManyToOne
    private Tva tva;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<Media> medias;
}
