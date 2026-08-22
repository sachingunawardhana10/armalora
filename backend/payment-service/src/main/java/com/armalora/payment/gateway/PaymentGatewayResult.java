package com.armalora.payment.gateway;

import com.armalora.payment.entity.PaymentGateway;
import com.armalora.payment.entity.PaymentStatus;

public record PaymentGatewayResult(PaymentGateway gateway, PaymentStatus status, String gatewayTransactionId) {

}