package com.jorge.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartDTO implements Serializable {
    private String userId;
    private List<CartItemDTO> items = new ArrayList<>();
    private BigDecimal total = BigDecimal.ZERO;
    private Integer totalItems = 0;
}