package com.armalora.notification.service;

import com.armalora.notification.dto.CreateNotificationRequest;
import com.armalora.notification.dto.NotificationResponse;
import com.armalora.notification.entity.Notification;
import com.armalora.notification.entity.NotificationStatus;
import com.armalora.notification.repository.NotificationRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationService(
            NotificationRepository notificationRepository
    ) {
        this.notificationRepository =
                notificationRepository;
    }

    @Transactional
    public NotificationResponse createNotification(
            CreateNotificationRequest request
    ) {

        Notification notification =
                new Notification();

        notification.setUserId(
                request.getUserId()
        );

        notification.setOrderId(
                request.getOrderId()
        );

        notification.setType(
                request.getType()
        );

        notification.setTitle(
                request.getTitle()
        );

        notification.setMessage(
                request.getMessage()
        );

        notification.setStatus(
                NotificationStatus.PENDING
        );

        Notification savedNotification =
                notificationRepository.save(
                        notification
                );

        return convertToResponse(
                savedNotification
        );
    }

    @Transactional(readOnly = true)
    public List<NotificationResponse>
    getNotificationsByUserId(
            Long userId
    ) {

        return notificationRepository
                .findByUserIdOrderByCreatedAtDesc(
                        userId
                )
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<NotificationResponse>
    getNotificationsByOrderId(
            Long orderId
    ) {

        return notificationRepository
                .findByOrderIdOrderByCreatedAtDesc(
                        orderId
                )
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    @Transactional
    public NotificationResponse markAsSent(
            Long id
    ) {

        Notification notification =
                notificationRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Notification not found: "
                                                + id
                                )
                        );

        notification.setStatus(
                NotificationStatus.SENT
        );

        notification.setSentAt(
                LocalDateTime.now()
        );

        Notification updated =
                notificationRepository.save(
                        notification
                );

        return convertToResponse(updated);
    }

    private NotificationResponse convertToResponse(
            Notification notification
    ) {

        NotificationResponse response =
                new NotificationResponse();

        response.setId(
                notification.getId()
        );

        response.setUserId(
                notification.getUserId()
        );

        response.setOrderId(
                notification.getOrderId()
        );

        response.setType(
                notification.getType()
        );

        response.setTitle(
                notification.getTitle()
        );

        response.setMessage(
                notification.getMessage()
        );

        response.setStatus(
                notification.getStatus()
        );

        response.setCreatedAt(
                notification.getCreatedAt()
        );

        response.setSentAt(
                notification.getSentAt()
        );

        return response;
    }
}