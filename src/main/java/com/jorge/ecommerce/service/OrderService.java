package com.jorge.ecommerce.service;

import com.jorge.ecommerce.dto.CreateOrderDTO;
import com.jorge.ecommerce.dto.OrderDTO;
import com.jorge.ecommerce.dto.OrderItemDTO;
import com.jorge.ecommerce.dto.CartDTO;
import com.jorge.ecommerce.entity.*;
import com.jorge.ecommerce.enums.OrderStatus;
import com.jorge.ecommerce.exception.BadRequestException;
import com.jorge.ecommerce.exception.ResourceNotFoundException;
import com.jorge.ecommerce.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.jorge.ecommerce.dto.NotificationDTO;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final CouponRepository couponRepository;
    private final CartService cartService;
    private final NotificationService notificationService;

    @Transactional
    public OrderDTO createOrder(String userEmail, CreateOrderDTO createOrderDTO) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        CartDTO cart = cartService.getCart(userEmail);

        if (cart.getItems() == null || cart.getItems().isEmpty()) {
            throw new BadRequestException("Cart is empty");
        }

        Order order = new Order();
        order.setUser(user);
        order.setStatus(OrderStatus.PENDING);
        order.setPaymentMethod(createOrderDTO.getPaymentMethod());
        order.setShippingAddress(createOrderDTO.getShippingAddress());
        order.setShippingCity(createOrderDTO.getShippingCity());
        order.setShippingPostalCode(createOrderDTO.getShippingPostalCode());
        order.setShippingCountry(createOrderDTO.getShippingCountry());
        order.setTrackingNumber(UUID.randomUUID().toString().substring(0, 8).toUpperCase());

        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal subtotal = BigDecimal.ZERO;

        for (var cartItem : cart.getItems()) {
            Product product = productRepository.findById(cartItem.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + cartItem.getProductId()));

            if (product.getStock() < cartItem.getQuantity()) {
                throw new BadRequestException("Not enough stock for: " + product.getName());
            }

            product.setStock(product.getStock() - cartItem.getQuantity());
            productRepository.save(product);

            // Notificación de stock bajo
            if (product.getStock() <= 5) {
                notificationService.sendLowStockNotification(product.getName(), product.getStock());
            }

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getPrice());
            orderItem.setSubtotal(cartItem.getSubtotal());
            orderItems.add(orderItem);

            subtotal = subtotal.add(cartItem.getSubtotal());
        }

        order.setOrderItems(orderItems);
        order.setSubtotal(subtotal);

        BigDecimal discount = BigDecimal.ZERO;
        if (createOrderDTO.getCouponCode() != null && !createOrderDTO.getCouponCode().isEmpty()) {
            discount = applyCoupon(createOrderDTO.getCouponCode(), subtotal);
        }

        BigDecimal tax = subtotal.subtract(discount).multiply(new BigDecimal("0.21"));
        BigDecimal total = subtotal.subtract(discount).add(tax);

        order.setDiscount(discount);
        order.setTax(tax);
        order.setTotal(total);

        Order savedOrder = orderRepository.save(order);
        cartService.clearCart(userEmail);

        notificationService.sendOrderNotification(userEmail, convertToDTO(savedOrder));

        return convertToDTO(savedOrder);
    }

    @Transactional(readOnly = true)
    public Page<OrderDTO> getUserOrders(String userEmail, Pageable pageable) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return orderRepository.findByUser(user, pageable).map(this::convertToDTO);
    }

    @Transactional(readOnly = true)
    public OrderDTO getOrderById(String userEmail, Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));

        if (!order.getUser().getEmail().equals(userEmail)) {
            throw new BadRequestException("You don't have permission to view this order");
        }

        return convertToDTO(order);
    }

    @Transactional
    public OrderDTO cancelOrder(String userEmail, Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));

        if (!order.getUser().getEmail().equals(userEmail)) {
            throw new BadRequestException("You don't have permission to cancel this order");
        }

        if (order.getStatus() != OrderStatus.PENDING) {
            throw new BadRequestException("Only pending orders can be cancelled");
        }

        order.getOrderItems().forEach(item -> {
            Product product = item.getProduct();
            product.setStock(product.getStock() + item.getQuantity());
            productRepository.save(product);
        });

        order.setStatus(OrderStatus.CANCELLED);
        return convertToDTO(orderRepository.save(order));
    }

    @Transactional(readOnly = true)
    public Page<OrderDTO> getAllOrders(Pageable pageable) {
        return orderRepository.findAll(pageable).map(this::convertToDTO);
    }

    @Transactional
    public OrderDTO updateOrderStatus(Long orderId, OrderStatus status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));
        order.setStatus(status);

        OrderDTO updatedOrder = convertToDTO(orderRepository.save(order));
        notificationService.sendOrderStatusNotification(order.getUser().getEmail(), updatedOrder);
        return updatedOrder;
    }

    private BigDecimal applyCoupon(String couponCode, BigDecimal subtotal) {
        return couponRepository.findByCode(couponCode)
                .map(coupon -> {
                    if (!coupon.getActive()) return BigDecimal.ZERO;
                    if (coupon.getDiscountPercentage() != null) {
                        return subtotal.multiply(coupon.getDiscountPercentage()
                                .divide(new BigDecimal("100")));
                    }
                    if (coupon.getDiscountAmount() != null) {
                        return coupon.getDiscountAmount();
                    }
                    return BigDecimal.ZERO;
                })
                .orElse(BigDecimal.ZERO);
    }

    private OrderDTO convertToDTO(Order order) {
        OrderDTO dto = new OrderDTO();
        dto.setId(order.getId());
        dto.setUserId(order.getUser().getId());
        dto.setUserEmail(order.getUser().getEmail());
        dto.setStatus(order.getStatus());
        dto.setSubtotal(order.getSubtotal());
        dto.setDiscount(order.getDiscount());
        dto.setTax(order.getTax());
        dto.setTotal(order.getTotal());
        dto.setPaymentMethod(order.getPaymentMethod());
        dto.setShippingAddress(order.getShippingAddress());
        dto.setShippingCity(order.getShippingCity());
        dto.setShippingPostalCode(order.getShippingPostalCode());
        dto.setShippingCountry(order.getShippingCountry());
        dto.setTrackingNumber(order.getTrackingNumber());
        dto.setCreatedAt(order.getCreatedAt());
        dto.setUpdatedAt(order.getUpdatedAt());

        if (order.getOrderItems() != null) {
            dto.setOrderItems(order.getOrderItems().stream()
                    .map(this::convertItemToDTO)
                    .collect(Collectors.toList()));
        }

        return dto;
    }

    private OrderItemDTO convertItemToDTO(OrderItem item) {
        OrderItemDTO dto = new OrderItemDTO();
        dto.setId(item.getId());
        dto.setProductId(item.getProduct().getId());
        dto.setProductName(item.getProduct().getName());
        dto.setProductImage(item.getProduct().getImageUrl());
        dto.setQuantity(item.getQuantity());
        dto.setPrice(item.getPrice());
        dto.setSubtotal(item.getSubtotal());
        return dto;
    }
}