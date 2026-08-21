package com.armalora.notification.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "notifications",
        indexes = {
                @Index(
                        name = "idx_notification_user_id",
                        columnList = "user_id"
                ),
                @Index(
                        name = "idx_notification_order_id",
                        columnList = "order_id"
                )
        }
)
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
            name = "user_id",
            nullable = false
    )
    private Long userId;

    @Column(
            name = "order_id"
    )
    private Long orderId;

    @Enumerated(EnumType.STRING)
    @Column(
            nullable = false,
            length = 50
    )
    private NotificationType type;

    @Column(
            nullable = false,
            length = 255
    )
    private String title;

    @Column(
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String message;

    @Enumerated(EnumType.STRING)
    @Column(
            nullable = false,
            length = 30
    )
    private NotificationStatus status;

    @Column(
            name = "created_at",
            nullable = false
    )
    private LocalDateTime createdAt;

    @Column(
            name = "sent_at"
    )
    private LocalDateTime sentAt;

    @PrePersist
    protected void onCreate() {

        createdAt = LocalDateTime.now();

        if (status == null) {
            status = NotificationStatus.PENDING;
        }
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public NotificationType getType() {
        return type;
    }

    public void setType(NotificationType type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public NotificationStatus getStatus() {
        return status;
    }

    public void setStatus(NotificationStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getSentAt() {
        return sentAt;
    }

    public void setSentAt(LocalDateTime sentAt) {
        this.sentAt = sentAt;
    }
}