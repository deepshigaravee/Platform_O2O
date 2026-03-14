package com.o2o.platform.integration;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import com.o2o.platform.dto.OrderDTO;
import com.o2o.platform.dto.PaymentDTO;
import com.o2o.platform.dto.ProductDTO;
import com.o2o.platform.dto.StoreDTO;
import com.o2o.platform.entity.PaymentMethod;
import com.o2o.platform.entity.PaymentStatus;
import com.o2o.platform.entity.User;
import com.o2o.platform.entity.UserRole;
import com.o2o.platform.repository.OrderRepository;
import com.o2o.platform.repository.ProductRepository;
import com.o2o.platform.repository.UserRepository;
import com.o2o.platform.request.CreateOrderRequest;
import com.o2o.platform.request.CreateProductRequest;
import com.o2o.platform.request.CreateStoreRequest;
import com.o2o.platform.request.PaymentRequest;
import com.o2o.platform.service.OrderService;
import com.o2o.platform.service.PaymentService;
import com.o2o.platform.service.ProductService;
import com.o2o.platform.service.StoreService;

@SpringBootTest
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:testdb2;DB_CLOSE_DELAY=-1;MODE=MySQL",
        "spring.datasource.driverClassName=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.jpa.show-sql=false",
        "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
        "security.jwt.secret=test-secret-key-which-is-long-enough-1234567890",
        "security.jwt.expiration-ms=3600000"
})
@Transactional
class OrderPaymentIntegrationTest {

    @Autowired
    private StoreService storeService;

    @Autowired
    private ProductService productService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Test
    void endToEndOrderAndPaymentFlow() {
        // Create owner user
        User owner = new User();
        owner.setName("Owner " + UUID.randomUUID());
        owner.setEmail("owner" + Instant.now().toEpochMilli() + "@test.com");
        owner.setPhone(String.valueOf(Instant.now().toEpochMilli()).substring(2, 12));
        owner.setPassword("encoded-owner-password");
        owner.setRole(UserRole.STORE_OWNER);
        owner.setActive(Boolean.TRUE);
        owner = userRepository.save(owner);

        // Create store
        CreateStoreRequest storeRequest = new CreateStoreRequest();
        storeRequest.setStoreName("Test Store");
        storeRequest.setAddress("123 Test Lane");
        storeRequest.setContactNumber("9999999999");
        storeRequest.setLatitude(12.9);
        storeRequest.setLongitude(77.6);
        StoreDTO storeDTO = storeService.createStore(owner.getUserId(), storeRequest);

        // Create product
        CreateProductRequest productRequest = new CreateProductRequest();
        productRequest.setProductName("T-Shirt");
        productRequest.setBrand("BrandX");
        productRequest.setCategory("Clothing");
        productRequest.setDescription("Soft cotton tee");
        productRequest.setPrice(new BigDecimal("100.00"));
        productRequest.setQuantity(10);
        productRequest.setStoreId(storeDTO.getStoreId());
        ProductDTO productDTO = productService.createProduct(productRequest);

        // Create customer user
        User customer = new User();
        customer.setName("Customer " + UUID.randomUUID());
        customer.setEmail("cust" + Instant.now().toEpochMilli() + "@test.com");
        customer.setPhone(String.valueOf(Instant.now().toEpochMilli()).substring(2, 12));
        customer.setPassword("encoded-customer-password");
        customer.setRole(UserRole.CUSTOMER);
        customer.setActive(Boolean.TRUE);
        customer = userRepository.save(customer);

        // Place order (qty 2 => total 200, advance 40, balance 160)
        CreateOrderRequest orderRequest = new CreateOrderRequest();
        orderRequest.setUserId(customer.getUserId());
        orderRequest.setProductId(productDTO.getProductId());
        orderRequest.setQuantity(2);
        OrderDTO orderDTO = orderService.createOrder(orderRequest);

        assertThat(orderDTO.getTotalAmount()).isEqualByComparingTo("200.00");
        assertThat(orderDTO.getAdvanceAmount()).isEqualByComparingTo("40.00");
        assertThat(orderDTO.getBalanceAmount()).isEqualByComparingTo("160.00");

        // Stock should decrease by 2
        assertThat(productRepository.findById(productDTO.getProductId()).orElseThrow().getQuantity()).isEqualTo(8);

        // Process full payment -> status PAID, order reserved, balance 0
        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setOrderId(orderDTO.getOrderId());
        paymentRequest.setMethod(PaymentMethod.UPI);
        paymentRequest.setAmount(new BigDecimal("200.00"));
        PaymentDTO paymentDTO = paymentService.processPayment(paymentRequest);

        assertThat(paymentDTO.getStatus()).isEqualTo(PaymentStatus.PAID);
        assertThat(paymentDTO.getAmount()).isEqualByComparingTo("200.00");

        var persistedOrder = orderRepository.findById(orderDTO.getOrderId()).orElseThrow();
        assertThat(persistedOrder.getOrderStatus().name()).isEqualTo("RESERVED");
        assertThat(persistedOrder.getPaymentStatus()).isEqualTo(PaymentStatus.PAID);
        assertThat(persistedOrder.getBalanceAmount()).isEqualByComparingTo("0.00");
    }
}
