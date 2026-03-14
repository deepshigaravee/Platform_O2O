package com.o2o.platform.service;

import java.util.List;

import com.o2o.platform.dto.OrderDTO;
import com.o2o.platform.request.CreateOrderRequest;

public interface OrderService {
    OrderDTO createOrder(CreateOrderRequest request);
    OrderDTO getOrderById(Long orderId);
    List<OrderDTO> getOrdersByUser(Long userId);
}
