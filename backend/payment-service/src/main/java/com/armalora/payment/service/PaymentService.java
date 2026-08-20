package com.armalora.payment.service;

import com.armalora.payment.dto.CreatePaymentRequest;
import com.armalora.payment.dto.PaymentResponse;
import com.armalora.payment.entity.Payment;
import com.armalora.payment.entity.PaymentStatus;
import com.armalora.payment.repository.PaymentRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentService(
            PaymentRepository paymentRepository
    ) {
        this.paymentRepository =
                paymentRepository;
    }

    @Transactional
    public PaymentResponse createPayment(
            CreatePaymentRequest request
    ) {

        if (paymentRepository.existsByOrderId(
                request.getOrderId()
        )) {

            throw new IllegalStateException(
                    "Payment already exists for order: "
                            + request.getOrderId()
            );
        }

        Payment payment =
                new Payment();

        payment.setOrderId(
                request.getOrderId()
        );

        payment.setUserId(
                request.getUserId()
        );

        payment.setAmount(
                request.getAmount()
        );

        payment.setPaymentMethod(
                request.getPaymentMethod()
        );

        /*
         * Temporary internal payment simulation.
         *
         * Real payment gateway integration
         * will be added in a later batch.
         */
        payment.setStatus(
                PaymentStatus.SUCCESS
        );

        payment.setTransactionReference(
                generateTransactionReference()
        );

        Payment savedPayment =
                paymentRepository.save(payment);

        return convertToResponse(
                savedPayment
        );
    }

    @Transactional(readOnly = true)
    public PaymentResponse getPaymentByOrderId(
            Long orderId
    ) {

        Payment payment =
                paymentRepository
                        .findByOrderId(orderId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Payment not found for order: "
                                                + orderId
                                )
                        );

        return convertToResponse(payment);
    }

    private String generateTransactionReference() {

        return "TXN-"
                + UUID.randomUUID()
                .toString()
                .substring(0, 12)
                .toUpperCase();
    }

    private PaymentResponse convertToResponse(
            Payment payment
    ) {

        PaymentResponse response =
                new PaymentResponse();

        response.setId(
                payment.getId()
        );

        response.setOrderId(
                payment.getOrderId()
        );

        response.setUserId(
                payment.getUserId()
        );

        response.setAmount(
                payment.getAmount()
        );

        response.setStatus(
                payment.getStatus()
        );

        response.setTransactionReference(
                payment.getTransactionReference()
        );

        response.setPaymentMethod(
                payment.getPaymentMethod()
        );

        response.setCreatedAt(
                payment.getCreatedAt()
        );

        response.setUpdatedAt(
                payment.getUpdatedAt()
        );

        return response;
    }
}