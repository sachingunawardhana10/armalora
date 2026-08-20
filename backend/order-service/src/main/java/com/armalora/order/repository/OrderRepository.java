package com.armalora.order.repository;

import com.armalora.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository
        extends JpaRepository<Order, Long> {

    Optional<Order> findByOrderNumber(
            String orderNumber
    );

    Optional<Order> findByOrderNumberAndUserId(
            String orderNumber,
            Long userId
    );

    List<Order> findByUserId(
            Long userId
    );
}