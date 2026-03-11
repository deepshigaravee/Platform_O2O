package com.o2o.clothing.service;

import com.o2o.clothing.dto.StoreCreateRequest;
import com.o2o.clothing.entity.Store;
import com.o2o.clothing.entity.User;
import com.o2o.clothing.repository.StoreRepository;
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
public class StoreServiceTest {

    @Mock
    private StoreRepository storeRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private StoreService storeService;

    private User owner;
    private StoreCreateRequest request;

    @BeforeEach
    void setUp() {
        owner = new User();
        owner.setId(1L);
        owner.setRole("STORE_OWNER");

        request = new StoreCreateRequest();
        request.setName("Fashion Hub");
        request.setAddress("123 Main St");
        request.setLatitude(40.7128);
        request.setLongitude(-74.0060);
    }

    @Test
    void registerStore_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(owner));
        
        Store savedStore = new Store();
        savedStore.setName("Fashion Hub");
        when(storeRepository.save(any(Store.class))).thenReturn(savedStore);

        Store result = storeService.registerStore(1L, request);

        assertNotNull(result);
        assertEquals("Fashion Hub", result.getName());
        verify(storeRepository, times(1)).save(any(Store.class));
    }

    @Test
    void registerStore_UserNotOwner_ThrowsException() {
        owner.setRole("CUSTOMER");
        when(userRepository.findById(1L)).thenReturn(Optional.of(owner));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            storeService.registerStore(1L, request);
        });

        assertEquals("User does not have permission to create a store.", exception.getMessage());
        verify(storeRepository, never()).save(any());
    }
}
