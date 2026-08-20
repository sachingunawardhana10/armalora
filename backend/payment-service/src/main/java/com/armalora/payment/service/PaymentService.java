package com.armalora.payment.service;

import com.armalora.payment.client.OrderClient;
import com.armalora.payment.dto.CheckoutRequest;
import com.armalora.payment.dto.OrderResponse;
import com.armalora.payment.dto.PaymentResponse;
import com.armalora.payment.entity.OrderStatus;
import com.armalora.payment.entity.Payment;
import com.armalora.payment.entity.PaymentStatus;
import com.armalora.payment.exception.DuplicatePaymentException;
import com.armalora.payment.exception.InvalidPaymentStatusException;
import com.armalora.payment.exception.PaymentNotFoundException;
import com.armalora.payment.provider.PaymentProvider;
import com.armalora.payment.provider.PaymentProviderRequest;
import com.armalora.payment.provider.PaymentProviderResponse;
import com.armalora.payment.provider.PaymentProviderStatus;
import com.armalora.payment.repository.PaymentRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderClient orderClient;
    private final PaymentProvider paymentProvider;

    public PaymentService(
            PaymentRepository paymentRepository,
            OrderClient orderClient,
            PaymentProvider paymentProvider
    ) {

        this.paymentRepository =
                paymentRepository;

        this.orderClient =
                orderClient;

        this.paymentProvider =
                paymentProvider;
    }

    // ============================================================
    // CHECKOUT
    // ============================================================

    @Transactional
    public PaymentResponse checkout(
            Long authenticatedUserId,
            CheckoutRequest request
    ) {

        OrderResponse order =
                orderClient.getOrderById(
                        request.getOrderId()
                );

        if (order == null) {

            throw new RuntimeException(
                    "Order not found: "
                            + request.getOrderId()
            );
        }

        // Verify ownership
        if (!authenticatedUserId.equals(
                order.getUserId()
        )) {

            throw new RuntimeException(
                    "You are not allowed to pay for this order"
            );
        }

        // Order must be pending
        if (order.getStatus()
                != OrderStatus.PENDING) {

            throw new RuntimeException(
                    "Only PENDING orders can be paid"
            );
        }

        // Prevent duplicate successful payment
        if (paymentRepository
                .existsByOrderIdAndStatus(
                        order.getId(),
                        PaymentStatus.SUCCESS
                )) {

            throw new DuplicatePaymentException(
                    "Order already has a successful payment"
            );
        }

        // Create payment
        Payment payment =
                new Payment();

        payment.setPaymentReference(
                generatePaymentReference()
        );

        payment.setOrderId(
                order.getId()
        );

        payment.setUserId(
                order.getUserId()
        );

        // Amount comes from Order Service
        payment.setAmount(
                order.getTotalAmount()
        );

        payment.setCurrency(
                "LKR"
        );

        payment.setPaymentMethod(
                request.getPaymentMethod()
        );

        payment.setStatus(
                PaymentStatus.PENDING
        );

        // Save
        Payment savedPayment =
                paymentRepository.save(
                        payment
                );

        // Provider request
        PaymentProviderRequest
                providerRequest =
                new PaymentProviderRequest();

        providerRequest.setPaymentReference(
                savedPayment
                        .getPaymentReference()
        );

        providerRequest.setAmount(
                savedPayment.getAmount()
        );

        providerRequest.setCurrency(
                savedPayment.getCurrency()
        );

        providerRequest.setPaymentMethod(
                savedPayment
                        .getPaymentMethod()
        );

        PaymentProviderResponse
                providerResponse =
                paymentProvider
                        .createPayment(
                                providerRequest
                        );

        savedPayment.setProviderReference(
                providerResponse
                        .getProviderReference()
        );

        if (providerResponse.getStatus()
                == PaymentProviderStatus.PENDING) {

            savedPayment.setStatus(
                    PaymentStatus.PENDING
            );
        }

        return convertToResponse(
                paymentRepository.save(
                        savedPayment
                )
        );
    }

    // ============================================================
    // PROCESS PAYMENT
    // ============================================================

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
                                new PaymentNotFoundException(
                                        paymentReference
                                )
                        );

        if (payment.getStatus()
                != PaymentStatus.PENDING) {

            throw new InvalidPaymentStatusException(
                    "Only PENDING payments can be processed"
            );
        }

        payment.setStatus(
                PaymentStatus.PROCESSING
        );

        paymentRepository.save(
                payment
        );

        PaymentProviderRequest
                providerRequest =
                new PaymentProviderRequest();

        providerRequest.setPaymentReference(
                payment.getPaymentReference()
        );

        providerRequest.setAmount(
                payment.getAmount()
        );

        providerRequest.setCurrency(
                payment.getCurrency()
        );

        providerRequest.setPaymentMethod(
                payment.getPaymentMethod()
        );

        PaymentProviderResponse
                providerResponse =
                paymentProvider.processPayment(
                        providerRequest
                );

        payment.setProviderReference(
                providerResponse
                        .getProviderReference()
        );

        if (providerResponse.getStatus()
                == PaymentProviderStatus.SUCCESS) {

            payment.setStatus(
                    PaymentStatus.SUCCESS
            );

        } else if (
                providerResponse.getStatus()
                        == PaymentProviderStatus.FAILED
        ) {

            payment.setStatus(
                    PaymentStatus.FAILED
            );

        } else if (
                providerResponse.getStatus()
                        == PaymentProviderStatus.CANCELLED
        ) {

            payment.setStatus(
                    PaymentStatus.CANCELLED
            );
        }

        Payment savedPayment =
                paymentRepository.save(
                        payment
                );

        return convertToResponse(
                savedPayment
        );
    }

    // ============================================================
    // GET PAYMENT
    // ============================================================

    @Transactional(readOnly = true)
    public PaymentResponse getPaymentById(
            Long id
    ) {

        Payment payment =
                paymentRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new PaymentNotFoundException(
                                        id
                                )
                        );

        return convertToResponse(
                payment
        );
    }

    @Transactional(readOnly = true)
    public PaymentResponse getPaymentByReference(
            String reference
    ) {

        Payment payment =
                paymentRepository
                        .findByPaymentReference(
                                reference
                        )
                        .orElseThrow(() ->
                                new PaymentNotFoundException(
                                        reference
                                )
                        );

        return convertToResponse(
                payment
        );
    }

    @Transactional(readOnly = true)
    public PaymentResponse getPaymentByOrderId(
            Long orderId
    ) {

        Payment payment =
                paymentRepository
                        .findByOrderId(
                                orderId
                        )
                        .orElseThrow(() ->
                                new PaymentNotFoundException(
                                        "Payment not found for order "
                                                + orderId
                                )
                        );

        return convertToResponse(
                payment
        );
    }

    @Transactional(readOnly = true)
    public List<PaymentResponse> getPaymentsByUserId(
            Long userId
    ) {

        return paymentRepository
                .findAllByUserId(userId)
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    // ============================================================
    // GENERATE REFERENCE
    // ============================================================

    private String generatePaymentReference() {

        return "PAY-"
                + UUID.randomUUID()
                .toString()
                .substring(
                        0,
                        8
                )
                .toUpperCase();
    }

    // ============================================================
    // RESPONSE
    // ============================================================

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

        response.setOrderId(
                payment.getOrderId()
        );

        response.setUserId(
                payment.getUserId()
        );

        response.setAmount(
                payment.getAmount()
        );

        response.setCurrency(
                payment.getCurrency()
        );

        response.setStatus(
                payment.getStatus()
        );

        response.setPaymentMethod(
                payment.getPaymentMethod()
        );

        response.setProviderReference(
                payment.getProviderReference()
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