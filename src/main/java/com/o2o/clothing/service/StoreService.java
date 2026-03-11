package com.o2o.clothing.service;

import com.o2o.clothing.dto.StoreCreateRequest;
import com.o2o.clothing.entity.Store;
import com.o2o.clothing.entity.User;
import com.o2o.clothing.repository.StoreRepository;
import com.o2o.clothing.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StoreService {

    private final StoreRepository storeRepository;
    private final UserRepository userRepository;

    @Autowired
    public StoreService(StoreRepository storeRepository, UserRepository userRepository) {
        this.storeRepository = storeRepository;
        this.userRepository = userRepository;
    }

    public Store registerStore(Long ownerId, StoreCreateRequest request) {
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new RuntimeException("Owner not found!"));

        if(!"STORE_OWNER".equals(owner.getRole())) {
            throw new RuntimeException("User does not have permission to create a store.");
        }

        Store store = new Store();
        store.setOwner(owner);
        store.setName(request.getName());
        store.setAddress(request.getAddress());
        store.setLatitude(request.getLatitude());
        store.setLongitude(request.getLongitude());

        return storeRepository.save(store);
    }

    public Store updateStore(Long id, StoreCreateRequest request) {
        Store store = storeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Store not found!"));
        if(request.getName() != null) store.setName(request.getName());
        if(request.getAddress() != null) store.setAddress(request.getAddress());
        if(request.getLatitude() != null) store.setLatitude(request.getLatitude());
        if(request.getLongitude() != null) store.setLongitude(request.getLongitude());

        return storeRepository.save(store);
    }

    public Store getStoreById(Long id) {
        return storeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Store not found!"));
    }

    public List<Store> getStoresByOwner(Long ownerId) {
        return storeRepository.findByOwnerId(ownerId);
    }
    
    public List<Store> getAllStores() {
        return storeRepository.findAllStores();
    }
}
