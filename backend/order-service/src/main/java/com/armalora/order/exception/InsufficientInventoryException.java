package com.armalora.order.exception;

public class InsufficientInventoryException
        extends RuntimeException {

    public InsufficientInventoryException(
            Long productId,
            Long variantId,
            Integer requested,
            Integer available) {

        super(
                "Insufficient inventory for product "
                        + productId
                        + ", variant "
                        + variantId
                        + ". Requested: "
                        + requested
                        + ", available: "
                        + available
        );
    }
}