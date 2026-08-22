package com.armalora.payment.gateway;

import com.armalora.payment.entity.PaymentStatus;

public class PaymentGatewayResult {

    private final PaymentStatus status;

    private final String gatewayTransactionId;

    private final String message;

    public PaymentGatewayResult(
            PaymentStatus status,
            String gatewayTransactionId,
            String message
    ) {
        this.status = status;
        this.gatewayTransactionId =
                gatewayTransactionId;
        this.message = message;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public String getGatewayTransactionId() {
        return gatewayTransactionId;
    }

    public String getMessage() {
        return message;
    }
}