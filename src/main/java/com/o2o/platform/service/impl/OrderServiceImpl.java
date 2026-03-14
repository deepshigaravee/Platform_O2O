package com.o2o.platform.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.springframework.stereotype.Service;

import com.o2o.platform.dto.OrderDTO;
import com.o2o.platform.entity.Order;
import com.o2o.platform.entity.OrderStatus;
import com.o2o.platform.entity.PaymentStatus;
import com.o2o.platform.entity.Product;
import com.o2o.platform.entity.User;
import com.o2o.platform.exception.ResourceNotFoundException;
import com.o2o.platform.repository.OrderRepository;
import com.o2o.platform.repository.ProductRepository;
import com.o2o.platform.repository.UserRepository;
import com.o2o.platform.request.CreateOrderRequest;
import com.o2o.platform.service.OrderService;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public OrderServiceImpl(OrderRepository orderRepository, UserRepository userRepository,
                            ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    @Override
    public OrderDTO createOrder(CreateOrderRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        if (request.getQuantity() == null || request.getQuantity() <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }
        if (product.getQuantity() < request.getQuantity()) {
            throw new IllegalArgumentException("Insufficient stock for this order");
        }

        BigDecimal totalAmount = product.getPrice().multiply(BigDecimal.valueOf(request.getQuantity()));
        BigDecimal advanceAmount = totalAmount.multiply(BigDecimal.valueOf(0.20)).setScale(2, RoundingMode.HALF_UP);
        BigDecimal balanceAmount = totalAmount.subtract(advanceAmount).setScale(2, RoundingMode.HALF_UP);

        Order order = new Order();
        order.setUser(user);
        order.setProduct(product);
        order.setQuantity(request.getQuantity());
        order.setTotalAmount(totalAmount.setScale(2, RoundingMode.HALF_UP));
        order.setAdvanceAmount(advanceAmount);
        order.setBalanceAmount(balanceAmount);
        order.setOrderStatus(OrderStatus.PENDING_PAYMENT);
        order.setPaymentStatus(PaymentStatus.PENDING);

        product.setQuantity(product.getQuantity() - request.getQuantity());
        productRepository.save(product);

        return toDTO(orderRepository.save(order));
    }

    @Override
    public OrderDTO getOrderById(Long orderId) {
        return toDTO(orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found")));
    }

    @Override
    public List<OrderDTO> getOrdersByUser(Long userId) {
        return orderRepository.findByUserUserId(userId).stream().map(this::toDTO).toList();
    }

    private OrderDTO toDTO(Order order) {
        OrderDTO dto = new OrderDTO();
        dto.setOrderId(order.getOrderId());
        dto.setUserId(order.getUser() != null ? order.getUser().getUserId() : null);
        dto.setProductId(order.getProduct() != null ? order.getProduct().getProductId() : null);
        dto.setQuantity(order.getQuantity());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setAdvanceAmount(order.getAdvanceAmount());
        dto.setBalanceAmount(order.getBalanceAmount());
        dto.setOrderStatus(order.getOrderStatus());
        dto.setPaymentStatus(order.getPaymentStatus());
        dto.setOrderDate(order.getOrderDate());
        return dto;
    }
}
