package com.o2o.platform.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.o2o.platform.entity.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByOrderOrderId(Long orderId);
}
