package com.jorge.ecommerce.repository;

import com.jorge.ecommerce.entity.Order;
import com.jorge.ecommerce.entity.User;
import com.jorge.ecommerce.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Page<Order> findByUser(User user, Pageable pageable);
    List<Order> findByStatus(OrderStatus status);

    @Query("SELECT SUM(o.total) FROM Order o WHERE o.status = 'DELIVERED'")
    java.math.BigDecimal getTotalRevenue();

    @Query("SELECT SUM(o.total) FROM Order o WHERE o.status = 'DELIVERED' AND o.createdAt >= :startDate")
    java.math.BigDecimal getRevenueFromDate(@Param("startDate") LocalDateTime startDate);

    Long countByStatus(OrderStatus status);
}