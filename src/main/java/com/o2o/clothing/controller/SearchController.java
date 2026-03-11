package com.o2o.clothing.controller;

import com.o2o.clothing.entity.Store;
import com.o2o.clothing.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/search")
public class SearchController {

    private final SearchService searchService;

    @Autowired
    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    @GetMapping("/nearby")
    public ResponseEntity<List<Store>> findNearbyStores(
            @RequestParam double lat,
            @RequestParam double lng) {
        // Defaults to 5.0 KM
        return ResponseEntity.ok(searchService.findNearbyStores(lat, lng, 5.0));
    }
}
