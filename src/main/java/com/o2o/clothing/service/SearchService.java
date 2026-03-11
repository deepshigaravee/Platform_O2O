package com.o2o.clothing.service;

import com.o2o.clothing.entity.Store;
import com.o2o.clothing.repository.StoreRepository;
import com.o2o.clothing.util.HaversineUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SearchService {

    private final StoreRepository storeRepository;

    @Autowired
    public SearchService(StoreRepository storeRepository) {
        this.storeRepository = storeRepository;
    }

    public List<Store> findNearbyStores(double userLat, double userLon, double radiusKm) {
        List<Store> allStores = storeRepository.findAllStores();
        
        return allStores.stream()
                .filter(store -> {
                    double distance = HaversineUtil.calculateDistance(
                            userLat, userLon, 
                            store.getLatitude(), store.getLongitude());
                    return distance <= radiusKm;
                })
                .collect(Collectors.toList());
    }
}
