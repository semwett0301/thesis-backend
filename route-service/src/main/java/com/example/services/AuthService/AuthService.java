package com.example.services.AuthService;

import com.example.model.dto.request.AuthRequest;
import com.example.model.dto.request.RefreshRequest;
import com.example.model.dto.response.AuthResponse;

public interface AuthService {
    AuthResponse loginUser(AuthRequest authRequest);
    AuthResponse registerUser(AuthRequest authRequest);
    AuthResponse getCurrentUser(String username);
    void logoutUser(String username);
    AuthResponse refreshToken(RefreshRequest refreshRequest);
}
