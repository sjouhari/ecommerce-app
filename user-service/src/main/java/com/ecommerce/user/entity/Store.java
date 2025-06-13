package com.ecommerce.user.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "stores")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Store extends BaseEntity {

    @Id
    @GeneratedValue
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Column(unique = true, nullable = false)
    private String name;

    private String address;
    private String phoneNumber;
    private String email;

    private boolean approved;
    private boolean rejected;

}
