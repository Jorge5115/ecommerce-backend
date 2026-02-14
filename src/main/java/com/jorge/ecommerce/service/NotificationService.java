package com.jorge.ecommerce.service;

import com.jorge.ecommerce.dto.NotificationDTO;
import com.jorge.ecommerce.dto.OrderDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final SimpMessagingTemplate messagingTemplate;

    public void sendOrderNotification(String userEmail, OrderDTO order) {
        NotificationDTO notification = new NotificationDTO(
                "ORDER_CREATED",
                "Pedido confirmado",
                "Tu pedido #" + order.getId() + " ha sido creado correctamente.",
                order
        );
        messagingTemplate.convertAndSendToUser(
                userEmail,
                "/queue/notifications",
                notification
        );

        sendAdminNotification(new NotificationDTO(
                "NEW_ORDER",
                "Nuevo pedido recibido",
                "El usuario " + userEmail + " ha realizado el pedido #" + order.getId(),
                order
        ));
    }

    public void sendOrderStatusNotification(String userEmail, OrderDTO order) {
        NotificationDTO notification = new NotificationDTO(
                "ORDER_STATUS_UPDATED",
                "Estado del pedido actualizado",
                "Tu pedido #" + order.getId() + " ahora está en estado: " + order.getStatus(),
                order
        );
        messagingTemplate.convertAndSendToUser(
                userEmail,
                "/queue/notifications",
                notification
        );
    }

    public void sendAdminNotification(NotificationDTO notification) {
        messagingTemplate.convertAndSend("/topic/admin", notification);
    }

    public void sendLowStockNotification(String productName, Integer stock) {
        NotificationDTO notification = new NotificationDTO(
                "LOW_STOCK",
                "Stock bajo",
                "El producto '" + productName + "' tiene solo " + stock + " unidades disponibles."
        );
        sendAdminNotification(notification);
    }
}