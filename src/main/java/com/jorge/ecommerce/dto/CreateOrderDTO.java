package com.jorge.ecommerce.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import com.jorge.ecommerce.enums.PaymentMethod;

@Data
public class CreateOrderDTO {

    @NotBlank(message = "Shipping address is required")
    private String shippingAddress;

    @NotBlank(message = "Shipping city is required")
    private String shippingCity;

    @NotBlank(message = "Shipping postal code is required")
    private String shippingPostalCode;

    @NotBlank(message = "Shipping country is required")
    private String shippingCountry;

    @NotNull(message = "Payment method is required")
    private PaymentMethod paymentMethod;

    private String couponCode;
}