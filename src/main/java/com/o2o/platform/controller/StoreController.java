package com.o2o.platform.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.o2o.platform.dto.StoreDTO;
import com.o2o.platform.request.CreateStoreRequest;
import com.o2o.platform.service.StoreService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/stores")
public class StoreController {

    private final StoreService storeService;

    public StoreController(StoreService storeService) {
        this.storeService = storeService;
    }

    @PostMapping("/{ownerId}")
    public ResponseEntity<StoreDTO> createStore(@PathVariable Long ownerId, @Valid @RequestBody CreateStoreRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(storeService.createStore(ownerId, request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<StoreDTO> getStoreById(@PathVariable("id") Long storeId) {
        return ResponseEntity.ok(storeService.getStoreById(storeId));
    }

    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<List<StoreDTO>> getStoresByOwner(@PathVariable Long ownerId) {
        return ResponseEntity.ok(storeService.getStoresByOwner(ownerId));
    }
}
