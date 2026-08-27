package com.armalora.payment.dto;

import com.armalora.payment.entity.PaymentStatus;

public class PaymentStatusUpdateRequest {

    private PaymentStatus status;

    private String gatewayTransactionId;

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(
            PaymentStatus status
    ) {
        this.status = status;
    }

    public String getGatewayTransactionId() {
        return gatewayTransactionId;
    }

    public void setGatewayTransactionId(
            String gatewayTransactionId
    ) {
        this.gatewayTransactionId =
                gatewayTransactionId;
    }
}