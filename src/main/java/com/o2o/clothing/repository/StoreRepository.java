package com.o2o.clothing.repository;

import com.o2o.clothing.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {
    List<Store> findByOwnerId(Long ownerId);
    
    @Query("SELECT s FROM Store s")
    List<Store> findAllStores();
}
