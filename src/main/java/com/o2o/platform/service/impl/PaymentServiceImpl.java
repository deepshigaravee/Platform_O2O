package com.o2o.platform.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.o2o.platform.dto.PaymentDTO;
import com.o2o.platform.entity.Order;
import com.o2o.platform.entity.OrderStatus;
import com.o2o.platform.entity.Payment;
import com.o2o.platform.entity.PaymentStatus;
import com.o2o.platform.exception.ResourceNotFoundException;
import com.o2o.platform.repository.OrderRepository;
import com.o2o.platform.repository.PaymentRepository;
import com.o2o.platform.request.PaymentRequest;
import com.o2o.platform.service.PaymentService;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;

    public PaymentServiceImpl(PaymentRepository paymentRepository, OrderRepository orderRepository) {
        this.paymentRepository = paymentRepository;
        this.orderRepository = orderRepository;
    }

    @Override
    public PaymentDTO processPayment(PaymentRequest request) {
        Order order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        BigDecimal amount = request.getAmount().setScale(2, RoundingMode.HALF_UP);
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Payment amount must be greater than 0");
        }

        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setUser(order.getUser());
        payment.setAmount(amount);
        payment.setPaymentMethod(request.getMethod());
        payment.setGatewayRefId(
                request.getGatewayRefId() == null || request.getGatewayRefId().isBlank()
                        ? UUID.randomUUID().toString()
                        : request.getGatewayRefId());

        if (amount.compareTo(order.getTotalAmount()) >= 0) {
            payment.setStatus(PaymentStatus.PAID);
            order.setPaymentStatus(PaymentStatus.PAID);
        } else {
            payment.setStatus(PaymentStatus.PARTIAL);
            order.setPaymentStatus(PaymentStatus.PARTIAL);
        }

        order.setOrderStatus(OrderStatus.RESERVED);
        BigDecimal remaining = order.getTotalAmount().subtract(amount).setScale(2, RoundingMode.HALF_UP);
        order.setBalanceAmount(remaining.compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : remaining);
        orderRepository.save(order);

        return toDTO(paymentRepository.save(payment));
    }

    @Override
    public PaymentDTO getPaymentByOrder(Long orderId) {
        return toDTO(paymentRepository.findByOrderOrderId(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found")));
    }

    private PaymentDTO toDTO(Payment payment) {
        PaymentDTO dto = new PaymentDTO();
        dto.setPaymentId(payment.getPaymentId());
        dto.setOrderId(payment.getOrder() != null ? payment.getOrder().getOrderId() : null);
        dto.setUserId(payment.getUser() != null ? payment.getUser().getUserId() : null);
        dto.setGatewayRefId(payment.getGatewayRefId());
        dto.setAmount(payment.getAmount());
        dto.setPaymentMethod(payment.getPaymentMethod());
        dto.setStatus(payment.getStatus());
        dto.setPaymentDate(payment.getPaymentDate());
        return dto;
    }
}
