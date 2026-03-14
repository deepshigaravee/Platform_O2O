package com.o2o.platform.service;

import java.util.List;

import com.o2o.platform.dto.StoreDTO;
import com.o2o.platform.request.CreateStoreRequest;

public interface StoreService {
    StoreDTO createStore(Long ownerId, CreateStoreRequest request);
    StoreDTO getStoreById(Long storeId);
    List<StoreDTO> getStoresByOwner(Long ownerId);
    List<StoreDTO> getAllStores();
}
