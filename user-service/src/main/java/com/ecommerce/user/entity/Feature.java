package com.ecommerce.user.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "features")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Feature {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String libelle; // Ex: Ajouter un utilisateur, Supprimer un utilisateur, Modifier un utilisateur, ...

    @Column(nullable = false)
    private String resourceName; // Ex: USER, PRODUCT, CATEGORY, ...

    @Column(nullable = false)
    private String action; // Ex: CREATE, READ, UPDATE, DELETE, ...
}
