package com.armalora.payment.gateway;

import com.armalora.payment.entity.Payment;
import com.armalora.payment.entity.PaymentGateway;
import com.armalora.payment.entity.PaymentStatus;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
@Primary
public class InternalPaymentGateway
        implements PaymentGatewayClient {

    @Override
    public PaymentGatewayResult processPayment(
            Payment payment
    ) {

        return new PaymentGatewayResult(
                PaymentGateway.INTERNAL,
                PaymentStatus.SUCCESS,
                "INT-" + payment.getOrderId()
        );
    }
}