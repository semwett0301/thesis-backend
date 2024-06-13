package com.example.api;

import com.example.model.dto.request.AuthRequest;
import com.example.model.dto.request.RefreshRequest;
import com.example.model.dto.response.AuthResponse;
import com.example.model.dto.response.UserResponse;
import com.example.services.AuthService.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AuthControllerTest {

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
    }

    @Test
    void getMe_Success() throws Exception {
        UserResponse userResponse = new UserResponse();
        when(authService.getUserByUsername(anyString())).thenReturn(userResponse);

        mockMvc.perform(get("/auth/me").principal(() -> "user"))
                .andExpect(status().isOk());
    }

    @Test
    void postLogin_Success() throws Exception {
        AuthResponse authResponse = new AuthResponse("access" ,"refresh");
        when(authService.loginUser(any(AuthRequest.class), anyString())).thenReturn(authResponse);

        mockMvc.perform(post("/auth/login")
                        .header("X-Finger-Print", "fingerprint")
                        .contentType("application/json")
                        .content("{\"username\":\"user\", \"password\":\"password\"}"))
                .andExpect(status().isOk());
    }

    @Test
    void postRegister_Success() throws Exception {
        AuthResponse authResponse = new AuthResponse("access" ,"refresh");
        when(authService.registerUser(any(AuthRequest.class), anyString())).thenReturn(authResponse);

        mockMvc.perform(post("/auth/register")
                        .header("X-Finger-Print", "fingerprint")
                        .contentType("application/json")
                        .content("{\"username\":\"user\", \"password\":\"password\"}"))
                .andExpect(status().isOk());
    }

    @Test
    void postLogout_Success() throws Exception {
        mockMvc.perform(post("/auth/logout")
                        .header("X-Finger-Print", "fingerprint")
                        .principal(() -> "user"))
                .andExpect(status().isOk());
    }

    @Test
    void postRefresh_Success() throws Exception {
        AuthResponse authResponse = new AuthResponse("access" ,"refresh");
        when(authService.refreshToken(any(RefreshRequest.class), anyString())).thenReturn(authResponse);

        mockMvc.perform(post("/auth/refresh")
                        .header("X-Finger-Print", "fingerprint")
                        .contentType("application/json")
                        .content("{\"refreshToken\":\"token\"}"))
                .andExpect(status().isOk());
    }
}
