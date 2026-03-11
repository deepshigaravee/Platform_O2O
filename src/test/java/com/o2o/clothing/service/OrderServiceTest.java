package com.o2o.clothing.service;

import com.o2o.clothing.dto.PreorderRequest;
import com.o2o.clothing.entity.Order;
import com.o2o.clothing.entity.Product;
import com.o2o.clothing.entity.User;
import com.o2o.clothing.repository.OrderRepository;
import com.o2o.clothing.repository.ProductRepository;
import com.o2o.clothing.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private OrderService orderService;

    private User user;
    private Product product;
    private PreorderRequest request;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);

        product = new Product();
        product.setId(1L);
        product.setPrice(100.0);
        product.setStockQuantity(5);

        request = new PreorderRequest();
        request.setUserId(1L);
        request.setProductId(1L);
    }

    @Test
    void createPreorder_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        
        Order savedOrder = new Order();
        savedOrder.setAdvancePayment(20.0);
        when(orderRepository.save(any(Order.class))).thenReturn(savedOrder);

        Order result = orderService.createPreorder(request);

        assertNotNull(result);
        assertEquals(20.0, result.getAdvancePayment()); // 20% of 100.0
        assertEquals(4, product.getStockQuantity()); // Stock reduced
        
        verify(productRepository, times(1)).save(product);
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void createPreorder_OutOfStock_ThrowsException() {
        product.setStockQuantity(0);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            orderService.createPreorder(request);
        });

        assertEquals("Product is out of stock!", exception.getMessage());
        verify(orderRepository, never()).save(any());
    }
}
