package com.armalora.order.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "payment-service")
public interface PaymentClient {

    @GetMapping("/api/payments/order/{orderId}")
    Object getPaymentByOrderId(
            @PathVariable("orderId") Long orderId
    );
}