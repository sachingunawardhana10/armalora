package com.armalora.payment.gateway;

import com.armalora.payment.entity.Payment;
import com.armalora.payment.entity.PaymentGateway;
import com.armalora.payment.entity.PaymentStatus;

import org.springframework.stereotype.Component;

@Component
public class MockPaymentGatewayClient
        implements PaymentGatewayClient {

    @Override
    public PaymentGatewayResult processPayment(
            Payment payment
    ) {

        return new PaymentGatewayResult(
                PaymentGateway.MOCK,
                PaymentStatus.SUCCESS,
                "MOCK-" + payment.getOrderId()
        );
    }
}