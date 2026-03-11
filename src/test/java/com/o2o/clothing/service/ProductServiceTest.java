package com.o2o.clothing.service;

import com.o2o.clothing.dto.ProductCreateRequest;
import com.o2o.clothing.entity.Product;
import com.o2o.clothing.entity.Store;
import com.o2o.clothing.repository.ProductRepository;
import com.o2o.clothing.repository.StoreRepository;
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
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private StoreRepository storeRepository;

    @InjectMocks
    private ProductService productService;

    private Store store;
    private ProductCreateRequest request;

    @BeforeEach
    void setUp() {
        store = new Store();
        store.setId(1L);

        request = new ProductCreateRequest();
        request.setStoreId(1L);
        request.setName("T-Shirt");
        request.setPrice(19.99);
        request.setStock(10);
        request.setDescription("Cotton T-Shirt");
    }

    @Test
    void addProduct_Success() {
        when(storeRepository.findById(1L)).thenReturn(Optional.of(store));
        
        Product savedProduct = new Product();
        savedProduct.setName("T-Shirt");
        when(productRepository.save(any(Product.class))).thenReturn(savedProduct);

        Product result = productService.addProduct(request);

        assertNotNull(result);
        assertEquals("T-Shirt", result.getName());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void addProduct_StoreNotFound_ThrowsException() {
        when(storeRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            productService.addProduct(request);
        });

        assertEquals("Store not found!", exception.getMessage());
        verify(productRepository, never()).save(any());
    }
}
