package com.jorge.ecommerce.controller;

import com.jorge.ecommerce.dto.NotificationDTO;
import com.jorge.ecommerce.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @MessageMapping("/ping")
    @SendTo("/topic/pong")
    public NotificationDTO ping() {
        return new NotificationDTO("PING", "Pong", "WebSocket connection active");
    }

    @PostMapping("/api/admin/notifications/broadcast")
    @PreAuthorize("hasRole('ADMIN')")
    public void broadcastNotification(@RequestBody NotificationDTO notification) {
        notificationService.sendAdminNotification(notification);
    }
}
