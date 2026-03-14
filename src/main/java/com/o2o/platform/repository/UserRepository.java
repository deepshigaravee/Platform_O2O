package com.o2o.platform.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.o2o.platform.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);
}
