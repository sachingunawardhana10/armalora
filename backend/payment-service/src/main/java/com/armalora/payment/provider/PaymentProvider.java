package com.armalora.payment.provider;

public interface PaymentProvider {

    PaymentProviderResponse createPayment(
            PaymentProviderRequest request
    );

    PaymentProviderResponse processPayment(
            PaymentProviderRequest request
    );

    PaymentProviderResponse refundPayment(
            PaymentProviderRequest request
    );
}