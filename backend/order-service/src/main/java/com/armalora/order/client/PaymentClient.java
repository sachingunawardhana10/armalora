package com.armalora.order.client;

import com.armalora.order.dto.PaymentRequest;
import com.armalora.order.dto.PaymentResponse;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(
        name = "payment-service"
)
public interface PaymentClient {

    @PostMapping("/api/payments")
    PaymentResponse createPayment(
            @RequestBody PaymentRequest request
    );

    @GetMapping("/api/payments/order/{orderId}")
    PaymentResponse getPaymentByOrderId(
            @PathVariable("orderId") Long orderId
    );
}