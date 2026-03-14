package com.o2o.platform.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.o2o.platform.dto.StoreDTO;
import com.o2o.platform.dto.UserDTO;
import com.o2o.platform.repository.StoreRepository;
import com.o2o.platform.repository.UserRepository;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final UserRepository userRepository;
    private final StoreRepository storeRepository;

    public AdminController(UserRepository userRepository, StoreRepository storeRepository) {
        this.userRepository = userRepository;
        this.storeRepository = storeRepository;
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> users() {
        List<UserDTO> users = userRepository.findAll().stream().map(user -> {
            UserDTO dto = new UserDTO();
            dto.setUserId(user.getUserId());
            dto.setName(user.getName());
            dto.setEmail(user.getEmail());
            dto.setPhone(user.getPhone());
            dto.setRole(user.getRole());
            dto.setLatitude(user.getLatitude());
            dto.setLongitude(user.getLongitude());
            dto.setActive(user.getActive());
            dto.setCreatedAt(user.getCreatedAt());
            return dto;
        }).toList();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/stores")
    public ResponseEntity<List<StoreDTO>> stores() {
        List<StoreDTO> stores = storeRepository.findAll().stream().map(store -> {
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
        }).toList();
        return ResponseEntity.ok(stores);
    }
}
