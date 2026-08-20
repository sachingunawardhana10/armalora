package com.armalora.payment.repository;

import com.armalora.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository
        extends JpaRepository<Payment, Long> {

    Optional<Payment> findByPaymentReference(
            String paymentReference
    );

    Optional<Payment> findByOrderNumber(
            String orderNumber
    );

    List<Payment> findByUserId(
            Long userId
    );

    boolean existsByOrderNumber(
            String orderNumber
    );
}