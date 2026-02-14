package com.jorge.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TopProductDTO {
    private Long productId;
    private String productName;
    private String productImage;
    private Long totalSold;
    private BigDecimal totalRevenue;
}