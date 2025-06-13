package com.ecommerce.user.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private boolean enabled;
    private boolean verified;
    private int verificationCode;
    private LocalDateTime verificationCodeExpireAt;

    @OneToOne(cascade = CascadeType.ALL)
    private Store store;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_profil",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "profil_id")
    )
    private List<Profil> profils;

    //newsletter subscription
    private boolean subscribed;
    private String subscriptionEmail;

}
