package com.o2o.platform.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.o2o.platform.entity.Store;

public interface StoreRepository extends JpaRepository<Store, Long> {
    List<Store> findByOwnerUserId(Long ownerId);
}
