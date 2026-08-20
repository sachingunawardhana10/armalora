package com.armalora.payment.controller;

import com.armalora.payment.dto.CreatePaymentRequest;
import com.armalora.payment.dto.PaymentResponse;
import com.armalora.payment.service.PaymentService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(
            PaymentService paymentService
    ) {
        this.paymentService =
                paymentService;
    }

    @PostMapping
    public ResponseEntity<PaymentResponse> createPayment(
            @Valid
            @RequestBody
            CreatePaymentRequest request
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        paymentService.createPayment(
                                request
                        )
                );
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<PaymentResponse>
    getPaymentByOrderId(
            @PathVariable Long orderId
    ) {

        return ResponseEntity.ok(
                paymentService.getPaymentByOrderId(
                        orderId
                )
        );
    }
}