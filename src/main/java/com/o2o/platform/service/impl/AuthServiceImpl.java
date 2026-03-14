package com.o2o.platform.service.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.o2o.platform.config.JwtUtil;
import com.o2o.platform.dto.LoginResponse;
import com.o2o.platform.dto.UserDTO;
import com.o2o.platform.entity.User;
import com.o2o.platform.entity.UserRole;
import com.o2o.platform.repository.UserRepository;
import com.o2o.platform.request.LoginRequest;
import com.o2o.platform.request.RegisterRequest;
import com.o2o.platform.service.AuthService;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public UserDTO register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }
        if (userRepository.existsByPhone(request.getPhone())) {
            throw new IllegalArgumentException("Phone already exists");
        }
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole() == null ? UserRole.CUSTOMER : request.getRole());
        user.setLatitude(request.getLatitude());
        user.setLongitude(request.getLongitude());
        user.setActive(request.getActive() == null ? Boolean.TRUE : request.getActive());
        return toDTO(userRepository.save(user));
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid credentials");
        }

        LoginResponse response = new LoginResponse();
        response.setToken(jwtUtil.generateToken(user.getEmail(), user.getRole().name()));
        response.setUser(toDTO(user));
        return response;
    }

    private UserDTO toDTO(User user) {
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
    }
}
