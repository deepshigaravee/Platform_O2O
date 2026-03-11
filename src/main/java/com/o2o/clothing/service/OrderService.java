package com.o2o.clothing.service;

import com.o2o.clothing.dto.PreorderRequest;
import com.o2o.clothing.entity.Order;
import com.o2o.clothing.entity.Product;
import com.o2o.clothing.entity.User;
import com.o2o.clothing.repository.OrderRepository;
import com.o2o.clothing.repository.ProductRepository;
import com.o2o.clothing.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository, UserRepository userRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    public Order createPreorder(PreorderRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found!"));
        
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found!"));

        if (product.getStockQuantity() <= 0) {
            throw new RuntimeException("Product is out of stock!");
        }

        Order order = new Order();
        order.setUser(user);
        order.setProduct(product);
        order.setTotalAmount(product.getPrice());
        order.setAdvancePayment(calculateAdvance(product.getPrice()));
        order.setStatus("RESERVED");
        order.setOrderDate(LocalDateTime.now());

        // Update stock
        product.setStockQuantity(product.getStockQuantity() - 1);
        productRepository.save(product);

        return orderRepository.save(order);
    }

    private Double calculateAdvance(Double price) {
        return price * 0.20; // 20% advance
    }

    public List<Order> getUserOrders(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    public List<Order> getStoreOrders(Long storeId) {
        return orderRepository.findByProductStoreId(storeId);
    }
}
