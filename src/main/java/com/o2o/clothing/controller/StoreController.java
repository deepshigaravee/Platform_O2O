package com.o2o.clothing.controller;

import com.o2o.clothing.dto.StoreCreateRequest;
import com.o2o.clothing.entity.Store;
import com.o2o.clothing.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/stores")
public class StoreController {

    private final StoreService storeService;

    @Autowired
    public StoreController(StoreService storeService) {
        this.storeService = storeService;
    }

    @PostMapping
    public ResponseEntity<Store> registerStore(@RequestParam Long ownerId, @RequestBody StoreCreateRequest request) {
        return ResponseEntity.ok(storeService.registerStore(ownerId, request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Store> updateStore(@PathVariable Long id, @RequestBody StoreCreateRequest request) {
        return ResponseEntity.ok(storeService.updateStore(id, request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Store> getStoreById(@PathVariable Long id) {
        return ResponseEntity.ok(storeService.getStoreById(id));
    }

    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<?> getStoresByOwner(@PathVariable Long ownerId) {
        return ResponseEntity.ok(storeService.getStoresByOwner(ownerId));
    }
}
