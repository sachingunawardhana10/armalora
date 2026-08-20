package com.armalora.order.dto;

import java.math.BigDecimal;

public class OrderItemResponse {

    private Long id;

    private Long productId;

    private Long variantId;

    private Integer quantity;

    private BigDecimal unitPrice;

    private BigDecimal subtotal;

    public OrderItemResponse() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(
            BigDecimal subtotal
    ) {
        this.subtotal = subtotal;
    }
}