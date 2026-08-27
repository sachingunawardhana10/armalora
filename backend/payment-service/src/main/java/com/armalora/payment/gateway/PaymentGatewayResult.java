package com.armalora.payment.gateway;

import com.armalora.payment.entity.PaymentGateway;
import com.armalora.payment.entity.PaymentStatus;

public class PaymentGatewayResult {

    private final PaymentGateway gateway;

    private final PaymentStatus status;

    private final String gatewayTransactionId;

    public PaymentGatewayResult(
            PaymentGateway gateway,
            PaymentStatus status,
            String gatewayTransactionId
    ) {
        this.gateway = gateway;
        this.status = status;
        this.gatewayTransactionId =
                gatewayTransactionId;
    }

    public PaymentGateway getGateway() {
        return gateway;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public String getGatewayTransactionId() {
        return gatewayTransactionId;
    }
}