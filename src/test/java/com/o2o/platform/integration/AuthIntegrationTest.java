package com.o2o.platform.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.o2o.platform.repository.UserRepository;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;MODE=MySQL",
        "spring.datasource.driverClassName=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.jpa.show-sql=false",
        "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
        "security.jwt.secret=test-secret-key-which-is-long-enough-1234567890",
        "security.jwt.expiration-ms=3600000"
})
class AuthIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Test
    void registerThenLogin() throws Exception {
        String suffix = UUID.randomUUID().toString().substring(0, 8);
        String email = "user" + suffix + "@test.com";
        String phone = String.valueOf(Instant.now().toEpochMilli()).substring(3, 13);
        String password = "Password123";

        Map<String, Object> registerPayload = new HashMap<>();
        registerPayload.put("name", "Test User");
        registerPayload.put("email", email);
        registerPayload.put("phone", phone);
        registerPayload.put("password", password);
        registerPayload.put("role", "CUSTOMER");

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerPayload)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userId").isNumber())
                .andExpect(jsonPath("$.email").value(email))
                .andExpect(jsonPath("$.role").value("CUSTOMER"));

        Map<String, Object> loginPayload = new HashMap<>();
        loginPayload.put("email", email);
        loginPayload.put("password", password);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginPayload)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty())
                .andExpect(jsonPath("$.user.email").value(email));
    }
}
