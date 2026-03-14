package com.o2o.platform.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.o2o.platform.entity.PaymentMethod;
import com.o2o.platform.entity.PaymentStatus;

public class PaymentDTO {
    private Long paymentId;
    private Long orderId;
    private Long userId;
    private String gatewayRefId;
    private BigDecimal amount;
    private PaymentMethod paymentMethod;
    private PaymentStatus status;
    private LocalDateTime paymentDate;

    public Long getPaymentId() { return paymentId; }
    public void setPaymentId(Long paymentId) { this.paymentId = paymentId; }
    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getGatewayRefId() { return gatewayRefId; }
    public void setGatewayRefId(String gatewayRefId) { this.gatewayRefId = gatewayRefId; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public PaymentMethod getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(PaymentMethod paymentMethod) { this.paymentMethod = paymentMethod; }
    public PaymentStatus getStatus() { return status; }
    public void setStatus(PaymentStatus status) { this.status = status; }
    public LocalDateTime getPaymentDate() { return paymentDate; }
    public void setPaymentDate(LocalDateTime paymentDate) { this.paymentDate = paymentDate; }
}
