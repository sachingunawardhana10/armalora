package com.armalora.order.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class CreateOrderRequest {

    @NotNull(message = "Shipping address is required")
    private String shippingAddress;

    @NotEmpty(message = "Order must contain at least one item")
    @Valid
    private List<OrderItemRequest> items;

    public CreateOrderRequest() {
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(
            String shippingAddress
    ) {
        this.shippingAddress = shippingAddress;
    }

    public List<OrderItemRequest> getItems() {
        return items;
    }

    public void setItems(
            List<OrderItemRequest> items
    ) {
        this.items = items;
    }
}