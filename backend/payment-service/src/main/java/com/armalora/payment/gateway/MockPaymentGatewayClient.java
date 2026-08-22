package com.armalora.payment.gateway;

import com.armalora.payment.entity.Payment;
import com.armalora.payment.entity.PaymentGateway;
import com.armalora.payment.entity.PaymentStatus;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class MockPaymentGatewayClient
        implements PaymentGatewayClient {

    @Override
    public PaymentGatewayResult processPayment(
            Payment payment
    ) {

        String gatewayTransactionId =
                "MOCK-"
                        + UUID.randomUUID()
                        .toString()
                        .substring(0, 12)
                        .toUpperCase();

        return new PaymentGatewayResult(
                PaymentGateway.INTERNAL,
                PaymentStatus.SUCCESS,
                gatewayTransactionId
        );
    }
}