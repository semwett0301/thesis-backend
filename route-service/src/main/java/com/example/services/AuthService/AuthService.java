package com.example.services.AuthService;

import com.example.model.request.AuthRequest;
import com.example.model.request.RefreshRequest;
import com.example.model.response.AuthResponse;
import com.example.model.response.UserResponse;

public interface AuthService {
    AuthResponse loginUser(AuthRequest authRequest, String fingerPrint);
    AuthResponse registerUser(AuthRequest authRequest, String fingerPrint);
    UserResponse getUserByUsername(String username);

    void logoutUser(String username, String fingerPrint);

    AuthResponse refreshToken(RefreshRequest refreshRequest, String fingerPrint);
}
