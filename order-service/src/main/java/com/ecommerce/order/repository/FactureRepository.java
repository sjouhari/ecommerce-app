package com.ecommerce.order.repository;

import com.ecommerce.order.entity.Facture;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FactureRepository extends JpaRepository<Facture, Long> {

    boolean existsByModePaymentId(Long modePaymentId);

}
