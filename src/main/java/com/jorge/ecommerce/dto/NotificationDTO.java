package com.jorge.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationDTO {
    private String type;
    private String title;
    private String message;
    private Object data;
    private LocalDateTime timestamp;

    public NotificationDTO(String type, String title, String message) {
        this.type = type;
        this.title = title;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

    public NotificationDTO(String type, String title, String message, Object data) {
        this(type, title, message);
        this.data = data;
    }
}