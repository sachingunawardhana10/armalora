package com.armalora.payment.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CheckoutRequest {

    @NotNull(message = "Order ID is required")
    private Long orderId;

    @NotNull(message = "Payment method is required")
    @Size(
            min = 2,
            max = 50,
            message = "Payment method must be between 2 and 50 characters"
    )
    private String paymentMethod;

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(
            String paymentMethod) {

        this.paymentMethod =
                paymentMethod;
    }
}