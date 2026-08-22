package com.armalora.order.entity;

public enum OrderStatus {

    PENDING,

    PENDING_PAYMENT,

    CONFIRMED,

    PAYMENT_FAILED,

    CANCELLED,

    PROCESSING,

    SHIPPED,

    DELIVERED,

    COMPLETED
}