package com.jorge.ecommerce.repository;

import com.jorge.ecommerce.entity.Order;
import com.jorge.ecommerce.entity.User;
import com.jorge.ecommerce.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Page<Order> findByUser(User user, Pageable pageable);
    List<Order> findByStatus(OrderStatus status);
    Page<Order> findAll(Pageable pageable);
}