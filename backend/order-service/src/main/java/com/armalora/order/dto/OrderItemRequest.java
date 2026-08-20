package com.armalora.order.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class OrderItemRequest {

    @NotNull(message = "Product ID is required")
    private Long productId;

    private Long variantId;

    @NotNull(message = "Quantity is required")
    @Min(
            value = 1,
            message = "Quantity must be at least 1"
    )
    private Integer quantity;

    @NotNull(message = "Unit price is required")
    @Min(
            value = 0,
            message = "Unit price cannot be negative"
    )
    private BigDecimal unitPrice;

    public OrderItemRequest() {
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(
            Long productId
    ) {
        this.productId = productId;
    }

    public Long getVariantId() {
        return variantId;
    }

    public void setVariantId(
            Long variantId
    ) {
        this.variantId = variantId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(
            Integer quantity
    ) {
        this.quantity = quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(
            BigDecimal unitPrice
    ) {
        this.unitPrice = unitPrice;
    }
}