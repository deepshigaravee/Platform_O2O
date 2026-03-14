package com.o2o.platform.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.o2o.platform.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserUserId(Long userId);
}
