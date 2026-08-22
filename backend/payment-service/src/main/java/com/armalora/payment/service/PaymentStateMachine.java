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

        if (current == next) {
            return false;
        }

        return switch (current) {

            case PENDING ->
                    next == PaymentStatus.PROCESSING
                            || next == PaymentStatus.CANCELLED;

            case PROCESSING ->
                    next == PaymentStatus.SUCCESS
                            || next == PaymentStatus.FAILED
                            || next == PaymentStatus.CANCELLED;

            case SUCCESS ->
                    next == PaymentStatus.REFUNDED;

            case FAILED,
                 CANCELLED,
                 REFUNDED ->
                    false;
        };
    }
}
