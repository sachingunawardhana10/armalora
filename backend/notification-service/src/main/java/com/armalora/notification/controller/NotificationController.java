package com.armalora.notification.controller;

import com.armalora.notification.dto.CreateNotificationRequest;
import com.armalora.notification.dto.NotificationResponse;
import com.armalora.notification.service.NotificationService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(
            NotificationService notificationService
    ) {
        this.notificationService =
                notificationService;
    }

    @PostMapping
    public ResponseEntity<NotificationResponse>
    createNotification(
            @Valid
            @RequestBody
            CreateNotificationRequest request
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        notificationService
                                .createNotification(request)
                );
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<NotificationResponse>>
    getNotificationsByUserId(
            @PathVariable Long userId
    ) {

        return ResponseEntity.ok(
                notificationService
                        .getNotificationsByUserId(userId)
        );
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<NotificationResponse>>
    getNotificationsByOrderId(
            @PathVariable Long orderId
    ) {

        return ResponseEntity.ok(
                notificationService
                        .getNotificationsByOrderId(orderId)
        );
    }

    @PatchMapping("/{id}/sent")
    public ResponseEntity<NotificationResponse>
    markAsSent(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                notificationService.markAsSent(id)
        );
    }
}