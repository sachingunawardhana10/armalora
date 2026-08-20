package com.armalora.payment.repository;

import com.armalora.payment.entity.Payment;
import com.armalora.payment.entity.PaymentStatus;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository
        extends JpaRepository<Payment, Long> {

    Optional<Payment> findByPaymentReference(
            String paymentReference
    );

    Optional<Payment> findByOrderId(
            Long orderId
    );

    List<Payment> findByUserId(
            Long userId
    );

    List<Payment> findByStatus(
            PaymentStatus status
    );
}