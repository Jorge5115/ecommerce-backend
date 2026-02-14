package com.jorge.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DashboardStatsDTO {
    private Long totalUsers;
    private Long totalProducts;
    private Long totalOrders;
    private Long pendingOrders;
    private Long completedOrders;
    private BigDecimal totalRevenue;
    private BigDecimal revenueThisMonth;
    private Long totalReviews;
    private Double averageRating;
}