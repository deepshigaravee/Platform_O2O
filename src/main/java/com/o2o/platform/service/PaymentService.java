package com.o2o.platform.service;

import com.o2o.platform.dto.PaymentDTO;
import com.o2o.platform.request.PaymentRequest;

public interface PaymentService {
    PaymentDTO processPayment(PaymentRequest request);
    PaymentDTO getPaymentByOrder(Long orderId);
}
