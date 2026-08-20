package com.armalora.payment.controller;

import com.armalora.payment.dto.PaymentRequest;
import com.armalora.payment.dto.PaymentResponse;
import com.armalora.payment.entity.PaymentStatus;
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

    @PostMapping
    public ResponseEntity<PaymentResponse>
    createPayment(
            @Valid @RequestBody PaymentRequest request) {

        return new ResponseEntity<>(
                paymentService.createPayment(request),
                HttpStatus.CREATED
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentResponse>
    getPaymentById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                paymentService.getPaymentById(id)
        );
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<PaymentResponse>
    getPaymentByOrderId(
            @PathVariable Long orderId) {

        return ResponseEntity.ok(
                paymentService.getPaymentByOrderId(
                        orderId
                )
        );
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PaymentResponse>>
    getPaymentsByUserId(
            @PathVariable Long userId) {

        return ResponseEntity.ok(
                paymentService.getPaymentsByUserId(
                        userId
                )
        );
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<PaymentResponse>
    updatePaymentStatus(
            @PathVariable Long id,
            @RequestParam PaymentStatus status) {

        return ResponseEntity.ok(
                paymentService.updatePaymentStatus(
                        id,
                        status
                )
        );
    }
}