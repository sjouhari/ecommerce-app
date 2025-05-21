package com.ecommerce.order.entity;

import com.ecommerce.order.enums.FactureStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "invoices")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Invoice extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private Order order;

    private double totalPrice;

    @Enumerated(EnumType.STRING)
    private FactureStatus status;

    @ManyToOne(cascade = CascadeType.ALL)
    private PaymentMethod paymentMethod;

}
