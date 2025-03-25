package com.ecommerce.order.repository;

import com.ecommerce.order.entity.ModePayment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ModePaymentRepository extends JpaRepository<ModePayment, Long> {

    Optional<ModePayment> findByName(String name);

}
