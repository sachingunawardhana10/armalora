package com.armalora.payment.service;

import com.armalora.payment.dto.CreatePaymentRequest;
import com.armalora.payment.dto.PaymentResponse;
import com.armalora.payment.entity.Payment;
import com.armalora.payment.entity.PaymentStatus;
import com.armalora.payment.repository.PaymentRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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

        if (paymentRepository
                .existsByOrderNumber(
                        request.getOrderNumber()
                )) {

            throw new IllegalStateException(
                    "Payment already exists for order: "
                            + request.getOrderNumber()
            );
        }

        Payment payment =
                new Payment();

        payment.setPaymentReference(
                generatePaymentReference()
        );

        payment.setOrderNumber(
                request.getOrderNumber()
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

        payment.setStatus(
                PaymentStatus.PENDING
        );

        Payment savedPayment =
                paymentRepository.save(payment);

        return convertToResponse(
                savedPayment
        );
    }

    @Transactional(readOnly = true)
    public PaymentResponse getPaymentById(
            Long id
    ) {

        Payment payment =
                paymentRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Payment not found: " + id
                                )
                        );

        return convertToResponse(payment);
    }

    @Transactional(readOnly = true)
    public PaymentResponse getPaymentByReference(
            String paymentReference
    ) {

        Payment payment =
                paymentRepository
                        .findByPaymentReference(
                                paymentReference
                        )
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Payment not found: "
                                                + paymentReference
                                )
                        );

        return convertToResponse(payment);
    }

    @Transactional(readOnly = true)
    public PaymentResponse getPaymentByOrderNumber(
            String orderNumber
    ) {

        Payment payment =
                paymentRepository
                        .findByOrderNumber(
                                orderNumber
                        )
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Payment not found for order: "
                                                + orderNumber
                                )
                        );

        return convertToResponse(payment);
    }

    @Transactional(readOnly = true)
    public List<PaymentResponse> getPaymentsByUserId(
            Long userId
    ) {

        return paymentRepository
                .findByUserId(userId)
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    @Transactional
    public PaymentResponse processPayment(
            String paymentReference
    ) {

        Payment payment =
                paymentRepository
                        .findByPaymentReference(
                                paymentReference
                        )
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Payment not found: "
                                                + paymentReference
                                )
                        );

        if (payment.getStatus()
                != PaymentStatus.PENDING) {

            throw new IllegalStateException(
                    "Payment is not in PENDING status"
            );
        }

        /*
         * Payment gateway simulation.
         *
         * Later this section will be replaced
         * with PayHere/Stripe/etc.
         */

        payment.setStatus(
                PaymentStatus.SUCCESS
        );

        payment.setTransactionId(
                generateTransactionId()
        );

        Payment updatedPayment =
                paymentRepository.save(payment);

        return convertToResponse(
                updatedPayment
        );
    }

    @Transactional
    public PaymentResponse failPayment(
            String paymentReference,
            String reason
    ) {

        Payment payment =
                paymentRepository
                        .findByPaymentReference(
                                paymentReference
                        )
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Payment not found: "
                                                + paymentReference
                                )
                        );

        if (payment.getStatus()
                != PaymentStatus.PENDING) {

            throw new IllegalStateException(
                    "Payment is not in PENDING status"
            );
        }

        payment.setStatus(
                PaymentStatus.FAILED
        );

        payment.setFailureReason(
                reason
        );

        Payment updatedPayment =
                paymentRepository.save(payment);

        return convertToResponse(
                updatedPayment
        );
    }

    @Transactional
    public PaymentResponse refundPayment(
            String paymentReference
    ) {

        Payment payment =
                paymentRepository
                        .findByPaymentReference(
                                paymentReference
                        )
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Payment not found: "
                                                + paymentReference
                                )
                        );

        if (payment.getStatus()
                != PaymentStatus.SUCCESS) {

            throw new IllegalStateException(
                    "Only successful payments can be refunded"
            );
        }

        payment.setStatus(
                PaymentStatus.REFUNDED
        );

        Payment updatedPayment =
                paymentRepository.save(payment);

        return convertToResponse(
                updatedPayment
        );
    }

    private String generatePaymentReference() {

        return "PAY-"
                + UUID.randomUUID()
                .toString()
                .substring(0, 8)
                .toUpperCase();
    }

    private String generateTransactionId() {

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

        response.setPaymentReference(
                payment.getPaymentReference()
        );

        response.setOrderNumber(
                payment.getOrderNumber()
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

        response.setPaymentMethod(
                payment.getPaymentMethod()
        );

        response.setTransactionId(
                payment.getTransactionId()
        );

        response.setFailureReason(
                payment.getFailureReason()
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