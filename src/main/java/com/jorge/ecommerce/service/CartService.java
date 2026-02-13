package com.jorge.ecommerce.service;

import com.jorge.ecommerce.dto.AddToCartDTO;
import com.jorge.ecommerce.dto.CartDTO;
import com.jorge.ecommerce.dto.CartItemDTO;
import com.jorge.ecommerce.entity.Product;
import com.jorge.ecommerce.exception.BadRequestException;
import com.jorge.ecommerce.exception.ResourceNotFoundException;
import com.jorge.ecommerce.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class CartService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ProductRepository productRepository;

    private static final String CART_KEY_PREFIX = "cart:";
    private static final long CART_EXPIRATION_DAYS = 7;

    private String getCartKey(String userId) {
        return CART_KEY_PREFIX + userId;
    }

    public CartDTO getCart(String userId) {
        String cartKey = getCartKey(userId);
        CartDTO cart = (CartDTO) redisTemplate.opsForValue().get(cartKey);

        if (cart == null) {
            cart = new CartDTO();
            cart.setUserId(userId);
        }

        return cart;
    }

    public CartDTO addToCart(String userId, AddToCartDTO addToCartDTO) {
        Product product = productRepository.findById(addToCartDTO.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + addToCartDTO.getProductId()));

        if (!product.getActive()) {
            throw new BadRequestException("Product is not available");
        }

        if (product.getStock() < addToCartDTO.getQuantity()) {
            throw new BadRequestException("Not enough stock. Available: " + product.getStock());
        }

        CartDTO cart = getCart(userId);
        List<CartItemDTO> items = cart.getItems();

        CartItemDTO existingItem = items.stream()
                .filter(item -> item.getProductId().equals(addToCartDTO.getProductId()))
                .findFirst()
                .orElse(null);

        if (existingItem != null) {
            int newQuantity = existingItem.getQuantity() + addToCartDTO.getQuantity();
            if (newQuantity > product.getStock()) {
                throw new BadRequestException("Not enough stock. Available: " + product.getStock());
            }
            existingItem.setQuantity(newQuantity);
            existingItem.setSubtotal(product.getPrice().multiply(BigDecimal.valueOf(newQuantity)));
        } else {
            CartItemDTO newItem = new CartItemDTO();
            newItem.setProductId(product.getId());
            newItem.setProductName(product.getName());
            newItem.setProductImage(product.getImageUrl());
            newItem.setPrice(product.getPrice());
            newItem.setQuantity(addToCartDTO.getQuantity());
            newItem.setSubtotal(product.getPrice().multiply(BigDecimal.valueOf(addToCartDTO.getQuantity())));
            items.add(newItem);
        }

        return saveCart(userId, cart);
    }

    public CartDTO updateCartItem(String userId, Long productId, Integer quantity) {
        CartDTO cart = getCart(userId);

        if (quantity <= 0) {
            return removeFromCart(userId, productId);
        }

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

        if (quantity > product.getStock()) {
            throw new BadRequestException("Not enough stock. Available: " + product.getStock());
        }

        cart.getItems().stream()
                .filter(item -> item.getProductId().equals(productId))
                .findFirst()
                .ifPresent(item -> {
                    item.setQuantity(quantity);
                    item.setSubtotal(item.getPrice().multiply(BigDecimal.valueOf(quantity)));
                });

        return saveCart(userId, cart);
    }

    public CartDTO removeFromCart(String userId, Long productId) {
        CartDTO cart = getCart(userId);
        cart.getItems().removeIf(item -> item.getProductId().equals(productId));
        return saveCart(userId, cart);
    }

    public void clearCart(String userId) {
        redisTemplate.delete(getCartKey(userId));
    }

    private CartDTO saveCart(String userId, CartDTO cart) {
        recalculateCart(cart);
        redisTemplate.opsForValue().set(
                getCartKey(userId),
                cart,
                CART_EXPIRATION_DAYS,
                TimeUnit.DAYS
        );
        return cart;
    }

    private void recalculateCart(CartDTO cart) {
        BigDecimal total = cart.getItems().stream()
                .map(CartItemDTO::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        int totalItems = cart.getItems().stream()
                .mapToInt(CartItemDTO::getQuantity)
                .sum();

        cart.setTotal(total);
        cart.setTotalItems(totalItems);
    }
}