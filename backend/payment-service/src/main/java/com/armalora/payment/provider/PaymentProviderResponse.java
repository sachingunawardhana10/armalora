package com.armalora.payment.provider;

public class PaymentProviderResponse {

    private PaymentProviderStatus status;

    private String providerReference;

    private String message;

    public PaymentProviderStatus getStatus() {
        return status;
    }

    public void setStatus(
            PaymentProviderStatus status) {

        this.status = status;
    }

    public String getProviderReference() {
        return providerReference;
    }

    public void setProviderReference(
            String providerReference) {

        this.providerReference =
                providerReference;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}