package com.armalora.product.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public class ProductVariantRequest {

    @NotBlank(message = "SKU is required")
    @Size(
            max = 100,
            message = "SKU must not exceed 100 characters"
    )
    private String sku;

    @Size(
            max = 50,
            message = "Size must not exceed 50 characters"
    )
    private String size;

    @Size(
            max = 50,
            message = "Color must not exceed 50 characters"
    )
    private String color;

    @DecimalMin(
            value = "0.00",
            message = "Additional price cannot be negative"
    )
    private BigDecimal additionalPrice =
            BigDecimal.ZERO;

    private Boolean active = true;

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public BigDecimal getAdditionalPrice() {
        return additionalPrice;
    }

    public void setAdditionalPrice(
            BigDecimal additionalPrice) {

        this.additionalPrice =
                additionalPrice;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}