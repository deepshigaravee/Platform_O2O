package com.o2o.platform.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.o2o.platform.dto.StoreDTO;
import com.o2o.platform.entity.Store;
import com.o2o.platform.repository.StoreRepository;
import com.o2o.platform.service.SearchService;
import com.o2o.platform.util.HaversineUtil;

@Service
public class SearchServiceImpl implements SearchService {

    private final StoreRepository storeRepository;

    public SearchServiceImpl(StoreRepository storeRepository) {
        this.storeRepository = storeRepository;
    }

    @Override
    public List<StoreDTO> searchNearbyStores(double latitude, double longitude, double radiusKm) {
        return storeRepository.findAll().stream()
                .filter(store -> Boolean.TRUE.equals(store.getActive()))
                .filter(store -> store.getLatitude() != null && store.getLongitude() != null)
                .filter(store -> HaversineUtil.distanceKm(latitude, longitude,
                        store.getLatitude(), store.getLongitude()) <= radiusKm)
                .map(this::toDTO)
                .toList();
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
