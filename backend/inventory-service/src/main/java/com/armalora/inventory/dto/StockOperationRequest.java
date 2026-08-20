package com.armalora.inventory.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class StockOperationRequest {

    @NotNull(message = "Amount is required")
    @Min(
            value = 1,
            message = "Amount must be greater than 0"
    )
    private Integer amount;

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }
}