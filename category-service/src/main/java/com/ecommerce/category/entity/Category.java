package com.ecommerce.category.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "categories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Category extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String description;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private List<SubCategory> subCategories;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private List<Size> sizes;

}
