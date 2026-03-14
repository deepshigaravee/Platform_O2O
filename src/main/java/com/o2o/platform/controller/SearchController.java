package com.o2o.platform.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.o2o.platform.dto.StoreDTO;
import com.o2o.platform.service.SearchService;

@RestController
@RequestMapping("/api/search")
public class SearchController {

    private final SearchService searchService;

    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    @GetMapping("/stores")
    public ResponseEntity<List<StoreDTO>> searchStores(
            @RequestParam double latitude,
            @RequestParam double longitude,
            @RequestParam(defaultValue = "5") double radiusKm) {
        return ResponseEntity.ok(searchService.searchNearbyStores(latitude, longitude, radiusKm));
    }

    @GetMapping("/nearby")
    public ResponseEntity<List<StoreDTO>> searchNearby(
            @RequestParam("lat") double latitude,
            @RequestParam("lng") double longitude,
            @RequestParam(defaultValue = "5") double radius) {
        return ResponseEntity.ok(searchService.searchNearbyStores(latitude, longitude, radius));
    }
}
