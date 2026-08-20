package com.armalora.payment.service;

import com.armalora.payment.dto.PaymentRequest;
import com.armalora.payment.dto.PaymentResponse;
import com.armalora.payment.entity.Payment;
import com.armalora.payment.entity.PaymentStatus;
import com.armalora.payment.exception.PaymentNotFoundException;
import com.armalora.payment.repository.PaymentRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentService(
            PaymentRepository paymentRepository) {

        this.paymentRepository =
                paymentRepository;
    }

    @Transactional
    public PaymentResponse createPayment(
            PaymentRequest request) {

        Payment payment =
                new Payment();

        payment.setPaymentReference(
                generatePaymentReference()
        );

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
            Long id) {

        Payment payment =
                paymentRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new PaymentNotFoundException(
                                        id
                                )
                        );

        return convertToResponse(payment);
    }

    @Transactional(readOnly = true)
    public PaymentResponse getPaymentByOrderId(
            Long orderId) {

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

    @Transactional(readOnly = true)
    public List<PaymentResponse> getPaymentsByUserId(
            Long userId) {

        return paymentRepository
                .findByUserId(userId)
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    @Transactional
    public PaymentResponse updatePaymentStatus(
            Long id,
            PaymentStatus status) {

        Payment payment =
                paymentRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new PaymentNotFoundException(
                                        id
                                )
                        );

        payment.setStatus(status);

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

    private PaymentResponse convertToResponse(
            Payment payment) {

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

        response.setStatus(
                payment.getStatus()
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