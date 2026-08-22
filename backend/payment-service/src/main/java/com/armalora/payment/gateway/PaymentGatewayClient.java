package com.armalora.payment.gateway;

import com.armalora.payment.entity.Payment;

public interface PaymentGatewayClient {

    PaymentGatewayResult processPayment(
            Payment payment
    );
}