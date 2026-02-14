package com.jorge.ecommerce.service;

import com.jorge.ecommerce.dto.DashboardStatsDTO;
import com.jorge.ecommerce.dto.TopProductDTO;
import com.jorge.ecommerce.dto.UserDTO;
import com.jorge.ecommerce.entity.User;
import com.jorge.ecommerce.enums.OrderStatus;
import com.jorge.ecommerce.enums.UserRole;
import com.jorge.ecommerce.exception.ResourceNotFoundException;
import com.jorge.ecommerce.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final ReviewRepository reviewRepository;

    @Transactional(readOnly = true)
    public DashboardStatsDTO getDashboardStats() {
        Long totalUsers = userRepository.count();
        Long totalProducts = productRepository.count();
        Long totalOrders = orderRepository.count();
        Long pendingOrders = orderRepository.countByStatus(OrderStatus.PENDING);
        Long completedOrders = orderRepository.countByStatus(OrderStatus.DELIVERED);

        BigDecimal totalRevenue = orderRepository.getTotalRevenue();
        if (totalRevenue == null) totalRevenue = BigDecimal.ZERO;

        LocalDateTime startOfMonth = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0);
        BigDecimal revenueThisMonth = orderRepository.getRevenueFromDate(startOfMonth);
        if (revenueThisMonth == null) revenueThisMonth = BigDecimal.ZERO;

        Long totalReviews = reviewRepository.count();
        Double averageRating = reviewRepository.findOverallAverageRating();
        if (averageRating == null) averageRating = 0.0;

        return new DashboardStatsDTO(
                totalUsers, totalProducts, totalOrders,
                pendingOrders, completedOrders,
                totalRevenue, revenueThisMonth,
                totalReviews, averageRating
        );
    }

    @Transactional(readOnly = true)
    public List<TopProductDTO> getTopProducts(int limit) {
        return productRepository.findTopSellingProducts(PageRequest.of(0, limit))
                .stream()
                .map(row -> new TopProductDTO(
                        ((Number) row[0]).longValue(),
                        (String) row[1],
                        (String) row[2],
                        ((Number) row[3]).longValue(),
                        (BigDecimal) row[4]
                ))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::convertUserToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public UserDTO toggleUserRole(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        user.setRole(user.getRole() == UserRole.ADMIN ? UserRole.CUSTOMER : UserRole.ADMIN);
        return convertUserToDTO(userRepository.save(user));
    }

    @Transactional
    public void deleteUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found");
        }
        userRepository.deleteById(userId);
    }

    private UserDTO convertUserToDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setPhone(user.getPhone());
        dto.setRole(user.getRole());
        dto.setCreatedAt(user.getCreatedAt());
        return dto;
    }
}