package com.armalora.payment.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "payments",
        indexes = {
                @Index(
                        name = "idx_payment_order_id",
                        columnList = "order_id"
                )
        }
)
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
            name = "order_id",
            nullable = false,
            unique = true
    )
    private Long orderId;

    @Column(
            name = "user_id",
            nullable = false
    )
    private Long userId;

    @Column(
            nullable = false,
            precision = 12,
            scale = 2
    )
    private BigDecimal amount;

    @Column(
            nullable = false,
            length = 3
    )
    private String currency;

    @Enumerated(EnumType.STRING)
    @Column(
            nullable = false,
            length = 30
    )
    private PaymentStatus status;

    @Column(
            name = "payment_reference",
            nullable = false,
            unique = true
    )
    private String paymentReference;

    @Column(
            name = "transaction_reference",
            unique = true
    )
    private String transactionReference;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "gateway",
            nullable = false,
            length = 30
    )
    private PaymentGateway gateway;

    @Column(
            name = "gateway_transaction_id"
    )
    private String gatewayTransactionId;

    @Column(
            name = "payment_method"
    )
    private String paymentMethod;

    @Column(
            name = "created_at",
            nullable = false
    )
    private LocalDateTime createdAt;

    @Column(
            name = "updated_at"
    )
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {

        LocalDateTime now =
                LocalDateTime.now();

        createdAt = now;
        updatedAt = now;

        if (status == null) {
            status = PaymentStatus.PENDING;
        }

        if (currency == null ||
                currency.isBlank()) {

            currency = "LKR";
        }

        if (paymentReference == null ||
                paymentReference.isBlank()) {

            paymentReference =
                    generatePaymentReference();
        }

        if (transactionReference == null ||
                transactionReference.isBlank()) {

            transactionReference =
                    generateTransactionReference();
        }

        if (gateway == null) {
            gateway =
                    PaymentGateway.INTERNAL;
        }
    }

    @PreUpdate
    protected void onUpdate() {

        updatedAt =
                LocalDateTime.now();
    }

    private String generatePaymentReference() {

        return "PAY-"
                + java.util.UUID.randomUUID()
                .toString()
                .substring(0, 12)
                .toUpperCase();
    }

    private String generateTransactionReference() {

        return "TXN-"
                + java.util.UUID.randomUUID()
                .toString()
                .substring(0, 12)
                .toUpperCase();
    }

    public Long getId() {
        return id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

    public String getPaymentReference() {
        return paymentReference;
    }

    public void setPaymentReference(
            String paymentReference
    ) {
        this.paymentReference =
                paymentReference;
    }

    public String getTransactionReference() {
        return transactionReference;
    }

    public void setTransactionReference(
            String transactionReference
    ) {
        this.transactionReference =
                transactionReference;
    }

    public PaymentGateway getGateway() {
        return gateway;
    }

    public void setGateway(
            PaymentGateway gateway
    ) {
        this.gateway = gateway;
    }

    public String getGatewayTransactionId() {
        return gatewayTransactionId;
    }

    public void setGatewayTransactionId(
            String gatewayTransactionId
    ) {
        this.gatewayTransactionId =
                gatewayTransactionId;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(
            String paymentMethod
    ) {
        this.paymentMethod =
                paymentMethod;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}