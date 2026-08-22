package com.armalora.payment.service;

import com.armalora.payment.dto.CreatePaymentRequest;
import com.armalora.payment.dto.PaymentResponse;
import com.armalora.payment.dto.PaymentStatusUpdateRequest;
import com.armalora.payment.entity.Payment;
import com.armalora.payment.entity.PaymentStatus;
import com.armalora.payment.gateway.PaymentGatewayClient;
import com.armalora.payment.gateway.PaymentGatewayResult;
import com.armalora.payment.repository.PaymentRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentGatewayClient paymentGatewayClient;
    private final PaymentStateMachine paymentStateMachine;

    public PaymentService(
            PaymentRepository paymentRepository,
            PaymentGatewayClient paymentGatewayClient,
            PaymentStateMachine paymentStateMachine
    ) {
        this.paymentRepository = paymentRepository;
        this.paymentGatewayClient = paymentGatewayClient;
        this.paymentStateMachine = paymentStateMachine;
    }

    /**
     * Creates a new payment record.
     *
     * The payment initially starts in PENDING state.
     * Actual processing is handled separately by processPayment().
     */
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

        Payment payment = new Payment();

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
         * New payment must begin in PENDING state.
         *
         * The gateway processing will happen through
         * processPayment().
         */
        payment.setStatus(
                PaymentStatus.PENDING
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

    /**
     * Gets payment by order ID.
     */
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

        return convertToResponse(
                payment
        );
    }

    /**
     * Gets payment by payment ID.
     */
    @Transactional(readOnly = true)
    public PaymentResponse getPaymentById(
            Long paymentId
    ) {

        Payment payment =
                paymentRepository
                        .findById(paymentId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Payment not found: "
                                                + paymentId
                                )
                        );

        return convertToResponse(
                payment
        );
    }

    /**
     * Processes a payment through the configured gateway.
     */
    @Transactional
    public PaymentResponse processPayment(
            Long paymentId
    ) {

        Payment payment =
                paymentRepository
                        .findById(paymentId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Payment not found: "
                                                + paymentId
                                )
                        );

        /*
         * Payment must be PENDING before processing.
         */
        if (!paymentStateMachine.isValidTransition(
                payment.getStatus(),
                PaymentStatus.PROCESSING
        )) {

            throw new IllegalStateException(
                    "Payment cannot be processed from status: "
                            + payment.getStatus()
            );
        }

        /*
         * Move payment to PROCESSING.
         */
        payment.setStatus(
                PaymentStatus.PROCESSING
        );

        paymentRepository.save(payment);

        PaymentGatewayResult result;

        try {

            result =
                    paymentGatewayClient
                            .processPayment(payment);

        } catch (Exception exception) {

            payment.setStatus(
                    PaymentStatus.FAILED
            );

            paymentRepository.save(payment);

            throw new IllegalStateException(
                    "Payment processing failed",
                    exception
            );
        }

        /*
         * Validate gateway result.
         */
        if (result == null) {

            payment.setStatus(
                    PaymentStatus.FAILED
            );

            paymentRepository.save(payment);

            throw new IllegalStateException(
                    "Payment gateway returned no result"
            );
        }

        PaymentStatus gatewayStatus =
                result.getStatus();

        /*
         * Validate PROCESSING -> SUCCESS/FAILED/CANCELLED.
         */
        if (!paymentStateMachine.isValidTransition(
                PaymentStatus.PROCESSING,
                gatewayStatus
        )) {

            payment.setStatus(
                    PaymentStatus.FAILED
            );

            paymentRepository.save(payment);

            throw new IllegalStateException(
                    "Invalid gateway payment status: "
                            + gatewayStatus
            );
        }

        payment.setStatus(
                gatewayStatus
        );

        if (result.getGatewayTransactionId()
                != null) {

            payment.setGatewayTransactionId(
                    result.getGatewayTransactionId()
            );
        }

        Payment updatedPayment =
                paymentRepository.save(payment);

        return convertToResponse(
                updatedPayment
        );
    }

    /**
     * Manually updates payment status.
     *
     * Used for administrative/payment-gateway callbacks
     * where required.
     */
    @Transactional
    public PaymentResponse updatePaymentStatus(
            Long paymentId,
            PaymentStatusUpdateRequest request
    ) {

        Payment payment =
                paymentRepository
                        .findById(paymentId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Payment not found: "
                                                + paymentId
                                )
                        );

        PaymentStatus currentStatus =
                payment.getStatus();

        PaymentStatus nextStatus =
                request.getStatus();

        if (!paymentStateMachine.isValidTransition(
                currentStatus,
                nextStatus
        )) {

            throw new IllegalStateException(
                    "Invalid payment status transition: "
                            + currentStatus
                            + " -> "
                            + nextStatus
            );
        }

        payment.setStatus(
                nextStatus
        );

        if (request.getGatewayTransactionId()
                != null
                && !request
                .getGatewayTransactionId()
                .isBlank()) {

            payment.setGatewayTransactionId(
                    request.getGatewayTransactionId()
            );
        }

        Payment updatedPayment =
                paymentRepository.save(payment);

        return convertToResponse(
                updatedPayment
        );
    }

    /**
     * Generates an internal transaction reference.
     */
    private String generateTransactionReference() {

        return "TXN-"
                + UUID.randomUUID()
                .toString()
                .substring(0, 12)
                .toUpperCase();
    }

    /**
     * Converts Payment entity into API response.
     */
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

        response.setGateway(
                payment.getGateway()
        );

        response.setGatewayTransactionId(
                payment.getGatewayTransactionId()
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