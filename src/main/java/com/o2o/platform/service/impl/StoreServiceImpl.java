package com.o2o.platform.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.o2o.platform.dto.StoreDTO;
import com.o2o.platform.entity.Store;
import com.o2o.platform.entity.User;
import com.o2o.platform.entity.UserRole;
import com.o2o.platform.exception.ResourceNotFoundException;
import com.o2o.platform.repository.StoreRepository;
import com.o2o.platform.repository.UserRepository;
import com.o2o.platform.request.CreateStoreRequest;
import com.o2o.platform.service.StoreService;

@Service
public class StoreServiceImpl implements StoreService {

    private final StoreRepository storeRepository;
    private final UserRepository userRepository;

    public StoreServiceImpl(StoreRepository storeRepository, UserRepository userRepository) {
        this.storeRepository = storeRepository;
        this.userRepository = userRepository;
    }

    @Override
    public StoreDTO createStore(Long ownerId, CreateStoreRequest request) {
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new ResourceNotFoundException("Owner not found"));

        if (owner.getRole() != UserRole.STORE_OWNER) {
            throw new IllegalArgumentException("Only STORE_OWNER can create a store");
        }

        Store store = new Store();
        store.setStoreName(request.getStoreName());
        store.setAddress(request.getAddress());
        store.setContactNumber(request.getContactNumber());
        store.setLatitude(request.getLatitude());
        store.setLongitude(request.getLongitude());
        store.setActive(Boolean.TRUE);
        store.setOwner(owner);
        return toDTO(storeRepository.save(store));
    }

    @Override
    public List<StoreDTO> getAllStores() {
        return storeRepository.findAll().stream().map(this::toDTO).toList();
    }

    @Override
    public StoreDTO getStoreById(Long storeId) {
        return toDTO(storeRepository.findById(storeId)
                .orElseThrow(() -> new ResourceNotFoundException("Store not found")));
    }

    @Override
    public List<StoreDTO> getStoresByOwner(Long ownerId) {
        return storeRepository.findByOwnerUserId(ownerId).stream().map(this::toDTO).toList();
    }

    private StoreDTO toDTO(Store store) {
        StoreDTO dto = new StoreDTO();
        dto.setStoreId(store.getStoreId());
        dto.setStoreName(store.getStoreName());
        dto.setAddress(store.getAddress());
        dto.setContactNumber(store.getContactNumber());
        dto.setLatitude(store.getLatitude());
        dto.setLongitude(store.getLongitude());
        dto.setActive(store.getActive());
        dto.setCreatedAt(store.getCreatedAt());
        dto.setOwnerId(store.getOwner() != null ? store.getOwner().getUserId() : null);
        return dto;
    }
}
