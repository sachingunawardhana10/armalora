package com.armalora.notification.repository;

import com.armalora.notification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository
        extends JpaRepository<Notification, Long> {

    List<Notification> findByUserIdOrderByCreatedAtDesc(
            Long userId
    );

    List<Notification> findByOrderIdOrderByCreatedAtDesc(
            Long orderId
    );
}