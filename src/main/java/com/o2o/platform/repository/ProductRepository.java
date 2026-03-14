package com.o2o.platform.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.o2o.platform.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByStoreStoreId(Long storeId);
}
