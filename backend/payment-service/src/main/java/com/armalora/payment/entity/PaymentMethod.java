package com.armalora.payment.entity;

public enum PaymentMethod {

    CARD,
    CASH_ON_DELIVERY,
    BANK_TRANSFER,
    ONLINE ;

    private String paymentMethod;
    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(
            String paymentMethod
    ) {
        this.paymentMethod =
                paymentMethod;
    }
}