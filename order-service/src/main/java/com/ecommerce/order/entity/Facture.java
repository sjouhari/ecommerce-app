package com.ecommerce.order.entity;

import com.ecommerce.order.enums.FactureStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "factures")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Facture extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private Order order;

    private double totalPrice;

    @Enumerated(EnumType.STRING)
    private FactureStatus status;

    @ManyToOne(cascade = CascadeType.ALL)
    private ModePayment modePayment;

}
