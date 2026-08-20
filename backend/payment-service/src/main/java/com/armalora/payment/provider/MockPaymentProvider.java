package com.armalora.payment.provider;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class MockPaymentProvider
        implements PaymentProvider {

    @Override
    public PaymentProviderResponse createPayment(
            PaymentProviderRequest request) {

        PaymentProviderResponse response =
                new PaymentProviderResponse();

        response.setStatus(
                PaymentProviderStatus.PENDING
        );

        response.setProviderReference(
                generateProviderReference()
        );

        response.setMessage(
                "Payment created successfully"
        );

        return response;
    }

    @Override
    public PaymentProviderResponse processPayment(
            PaymentProviderRequest request) {

        PaymentProviderResponse response =
                new PaymentProviderResponse();

        response.setStatus(
                PaymentProviderStatus.SUCCESS
        );

        response.setProviderReference(
                generateProviderReference()
        );

        response.setMessage(
                "Mock payment processed successfully"
        );

        return response;
    }

    @Override
    public PaymentProviderResponse refundPayment(
            PaymentProviderRequest request) {

        PaymentProviderResponse response =
                new PaymentProviderResponse();

        response.setStatus(
                PaymentProviderStatus.SUCCESS
        );

        response.setProviderReference(
                generateProviderReference()
        );

        response.setMessage(
                "Mock refund processed successfully"
        );

        return response;
    }

    private String generateProviderReference() {

        return "MOCK-"
                + UUID.randomUUID()
                .toString()
                .substring(0, 8)
                .toUpperCase();
    }
}