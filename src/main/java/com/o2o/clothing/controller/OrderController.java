package com.o2o.clothing.controller;

import com.o2o.clothing.dto.PreorderRequest;
import com.o2o.clothing.entity.Order;
import com.o2o.clothing.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/preorder")
    public ResponseEntity<Order> createPreorder(@RequestBody PreorderRequest request) {
        return ResponseEntity.ok(orderService.createPreorder(request));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Order>> getUserOrders(@PathVariable Long userId) {
        return ResponseEntity.ok(orderService.getUserOrders(userId));
    }

    @GetMapping("/store/{storeId}")
    public ResponseEntity<List<Order>> getStoreOrders(@PathVariable Long storeId) {
        return ResponseEntity.ok(orderService.getStoreOrders(storeId));
    }
}
