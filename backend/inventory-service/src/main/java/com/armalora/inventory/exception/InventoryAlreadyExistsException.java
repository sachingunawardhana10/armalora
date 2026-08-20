package com.armalora.inventory.exception;

public class InventoryAlreadyExistsException
        extends RuntimeException {

    public InventoryAlreadyExistsException(
            String message) {

        super(message);
    }
}