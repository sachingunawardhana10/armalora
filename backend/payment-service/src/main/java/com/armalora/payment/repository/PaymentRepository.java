package com.armalora.payment.repository;

import com.armalora.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    // Custom query methods
    boolean existsByOrderId(Long orderId);

    Optional<Payment> findByOrderId(Long orderId);
}
