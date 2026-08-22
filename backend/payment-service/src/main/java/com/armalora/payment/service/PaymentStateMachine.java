package com.armalora.payment.service;

import com.armalora.payment.entity.PaymentStatus;

import org.springframework.stereotype.Component;

@Component
public class PaymentStateMachine {

    public boolean isValidTransition(
            PaymentStatus current,
            PaymentStatus next
    ) {

        if (current == null || next == null) {
            return false;
        }

        if (current == PaymentStatus.PENDING) {

            return next == PaymentStatus.PROCESSING
                    || next == PaymentStatus.CANCELLED;
        }

        if (current == PaymentStatus.PROCESSING) {

            return next == PaymentStatus.SUCCESS
                    || next == PaymentStatus.FAILED
                    || next == PaymentStatus.CANCELLED;
        }

        if (current == PaymentStatus.SUCCESS) {

            return next == PaymentStatus.REFUNDED;
        }

        return false;
    }
}