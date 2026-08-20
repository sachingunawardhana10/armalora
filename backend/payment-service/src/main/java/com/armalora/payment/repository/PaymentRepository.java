package com.armalora.payment.repository;

import com.armalora.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository
        extends JpaRepository<Payment, Long> {

    Optional<Payment> findByOrderId(
            Long orderId
    );

    boolean existsByOrderId(
            Long orderId
    );
}