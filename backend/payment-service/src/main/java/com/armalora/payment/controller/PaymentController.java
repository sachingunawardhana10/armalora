package com.armalora.payment.controller;

import com.armalora.payment.dto.CheckoutRequest;
import com.armalora.payment.dto.PaymentResponse;
import com.armalora.payment.service.PaymentService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(
            PaymentService paymentService) {

        this.paymentService =
                paymentService;
    }

    // ============================================================
    // CHECKOUT
    // ============================================================

    @PostMapping("/checkout")
    public ResponseEntity<PaymentResponse>
    checkout(
            @RequestHeader("X-User-Id")
            Long userId,

            @Valid
            @RequestBody
            CheckoutRequest request
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        paymentService.checkout(
                                userId,
                                request
                        )
                );
    }

    // ============================================================
    // PROCESS
    // ============================================================

    @PostMapping(
            "/{paymentReference}/process"
    )
    public ResponseEntity<PaymentResponse>
    processPayment(
            @PathVariable
            String paymentReference
    ) {

        return ResponseEntity.ok(
                paymentService.processPayment(
                        paymentReference
                )
        );
    }

    // ============================================================
    // GET BY ID
    // ============================================================

    @GetMapping("/{id}")
    public ResponseEntity<PaymentResponse>
    getPaymentById(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                paymentService.getPaymentById(id)
        );
    }

    // ============================================================
    // GET BY REFERENCE
    // ============================================================

    @GetMapping(
            "/reference/{reference}"
    )
    public ResponseEntity<PaymentResponse>
    getPaymentByReference(
            @PathVariable String reference
    ) {

        return ResponseEntity.ok(
                paymentService
                        .getPaymentByReference(
                                reference
                        )
        );
    }

    // ============================================================
    // GET BY ORDER
    // ============================================================

    @GetMapping(
            "/order/{orderId}"
    )
    public ResponseEntity<PaymentResponse>
    getPaymentByOrderId(
            @PathVariable Long orderId
    ) {

        return ResponseEntity.ok(
                paymentService
                        .getPaymentByOrderId(
                                orderId
                        )
        );
    }

    // ============================================================
    // GET USER PAYMENTS
    // ============================================================

    @GetMapping("/user")
    public ResponseEntity<List<PaymentResponse>>
    getUserPayments(
            @RequestHeader("X-User-Id")
            Long userId
    ) {

        return ResponseEntity.ok(
                paymentService
                        .getPaymentsByUserId(
                                userId
                        )
        );
    }
}