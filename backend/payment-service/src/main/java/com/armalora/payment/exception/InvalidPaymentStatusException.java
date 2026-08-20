package com.armalora.payment.exception;

public class InvalidPaymentStatusException
        extends RuntimeException {

    public InvalidPaymentStatusException(
            String message) {

        super(message);
    }
}