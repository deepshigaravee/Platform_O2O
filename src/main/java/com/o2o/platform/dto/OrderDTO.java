package com.o2o.platform.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.o2o.platform.entity.OrderStatus;

public class OrderDTO {
    private Long orderId;
    private Long userId;
    private Long productId;
    private Integer quantity;
    private BigDecimal totalAmount;
    private BigDecimal advanceAmount;
    private BigDecimal balanceAmount;
    private OrderStatus orderStatus;
    private com.o2o.platform.entity.PaymentStatus paymentStatus;
    private LocalDateTime orderDate;

    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
    public BigDecimal getAdvanceAmount() { return advanceAmount; }
    public void setAdvanceAmount(BigDecimal advanceAmount) { this.advanceAmount = advanceAmount; }
    public BigDecimal getBalanceAmount() { return balanceAmount; }
    public void setBalanceAmount(BigDecimal balanceAmount) { this.balanceAmount = balanceAmount; }
    public OrderStatus getOrderStatus() { return orderStatus; }
    public void setOrderStatus(OrderStatus orderStatus) { this.orderStatus = orderStatus; }
    public com.o2o.platform.entity.PaymentStatus getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(com.o2o.platform.entity.PaymentStatus paymentStatus) { this.paymentStatus = paymentStatus; }
    public LocalDateTime getOrderDate() { return orderDate; }
    public void setOrderDate(LocalDateTime orderDate) { this.orderDate = orderDate; }
}
