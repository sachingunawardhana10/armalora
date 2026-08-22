package com.armalora.payment.gateway;

import com.armalora.payment.entity.Payment;
import com.armalora.payment.entity.PaymentStatus;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class InternalPaymentGateway
        implements PaymentGatewayClient {

    @Override
    public PaymentGatewayResult processPayment(
            Payment payment
    ) {

        String transactionId =
                "INT-"
                        + UUID.randomUUID()
                        .toString()
                        .replace("-", "")
                        .substring(0, 16)
                        .toUpperCase();

        return new PaymentGatewayResult(
                PaymentStatus.SUCCESS,
                transactionId,
                "Payment processed successfully"
        );
    }
}