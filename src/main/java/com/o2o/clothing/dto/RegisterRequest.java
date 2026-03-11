package com.o2o.clothing.dto;

import lombok.Data;

@Data
public class RegisterRequest {
    private String username;
    private String password;
    private String role; // CUSTOMER or STORE_OWNER
    private String email;
    private String phoneNumber;
}
