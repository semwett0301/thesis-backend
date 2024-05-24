package com.example.services.AuthService;

import com.example.model.dto.request.AuthRequest;
import com.example.model.dto.request.RefreshRequest;
import com.example.model.dto.response.AuthResponse;
import com.example.model.dto.response.UserResponse;

public interface AuthService {
    AuthResponse loginUser(AuthRequest authRequest, String fingerPrint);
    AuthResponse registerUser(AuthRequest authRequest, String fingerPrint);
    UserResponse getUserByUsername(String username);
    void logoutUser(String username);
    AuthResponse refreshToken(RefreshRequest refreshRequest, String fingerPrint);
}
