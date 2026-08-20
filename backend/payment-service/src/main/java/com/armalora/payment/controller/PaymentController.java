package com.armalora.payment.controller;

import com.armalora.payment.dto.CreatePaymentRequest;
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
            PaymentService paymentService
    ) {
        this.paymentService =
                paymentService;
    }

    @PostMapping
    public ResponseEntity<PaymentResponse> createPayment(
            @Valid @RequestBody
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

    @GetMapping("/{id}")
    public ResponseEntity<PaymentResponse> getPayment(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                paymentService.getPaymentById(id)
        );
    }

    @GetMapping("/reference/{paymentReference}")
    public ResponseEntity<PaymentResponse> getPaymentByReference(
            @PathVariable String paymentReference
    ) {

        return ResponseEntity.ok(
                paymentService.getPaymentByReference(
                        paymentReference
                )
        );
    }

    @GetMapping("/order/{orderNumber}")
    public ResponseEntity<PaymentResponse> getPaymentByOrder(
            @PathVariable String orderNumber
    ) {

        return ResponseEntity.ok(
                paymentService.getPaymentByOrderNumber(
                        orderNumber
                )
        );
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PaymentResponse>> getUserPayments(
            @PathVariable Long userId
    ) {

        return ResponseEntity.ok(
                paymentService.getPaymentsByUserId(
                        userId
                )
        );
    }

    @PostMapping("/{paymentReference}/process")
    public ResponseEntity<PaymentResponse> processPayment(
            @PathVariable String paymentReference
    ) {

        return ResponseEntity.ok(
                paymentService.processPayment(
                        paymentReference
                )
        );
    }

    @PostMapping("/{paymentReference}/fail")
    public ResponseEntity<PaymentResponse> failPayment(
            @PathVariable String paymentReference,
            @RequestParam String reason
    ) {

        return ResponseEntity.ok(
                paymentService.failPayment(
                        paymentReference,
                        reason
                )
        );
    }

    @PostMapping("/{paymentReference}/refund")
    public ResponseEntity<PaymentResponse> refundPayment(
            @PathVariable String paymentReference
    ) {

        return ResponseEntity.ok(
                paymentService.refundPayment(
                        paymentReference
                )
        );
    }
}