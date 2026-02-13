package com.jorge.ecommerce.dto;

import com.jorge.ecommerce.enums.OrderStatus;
import com.jorge.ecommerce.enums.PaymentMethod;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderDTO {
    private Long id;
    private Long userId;
    private String userEmail;
    private List<OrderItemDTO> orderItems;
    private OrderStatus status;
    private BigDecimal subtotal;
    private BigDecimal discount;
    private BigDecimal tax;
    private BigDecimal total;
    private PaymentMethod paymentMethod;
    private String shippingAddress;
    private String shippingCity;
    private String shippingPostalCode;
    private String shippingCountry;
    private String trackingNumber;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}