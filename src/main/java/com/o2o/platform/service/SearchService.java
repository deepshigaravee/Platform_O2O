package com.o2o.platform.service;

import java.util.List;

import com.o2o.platform.dto.StoreDTO;

public interface SearchService {
    List<StoreDTO> searchNearbyStores(double latitude, double longitude, double radiusKm);
}
