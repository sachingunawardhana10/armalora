package com.armalora.inventory.exception;

public class InvalidInventoryOperationException
        extends RuntimeException {

    public InvalidInventoryOperationException(
            String message) {

        super(message);
    }
}