package com.o2o.platform.service;

import com.o2o.platform.dto.LoginResponse;
import com.o2o.platform.dto.UserDTO;
import com.o2o.platform.request.LoginRequest;
import com.o2o.platform.request.RegisterRequest;

public interface AuthService {
    UserDTO register(RegisterRequest request);
    LoginResponse login(LoginRequest request);
}
