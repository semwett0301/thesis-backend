package com.example.services.AuthService;

import com.example.model.dto.request.AuthRequest;
import com.example.model.dto.request.RefreshRequest;
import com.example.model.dto.response.AuthResponse;
import org.springframework.stereotype.Service;

@Service
public class ThesisAuthService implements AuthService{
    @Override
    public AuthResponse loginUser(AuthRequest authRequest) {
        return null;
    }

    @Override
    public AuthResponse registerUser(AuthRequest authRequest) {
        return null;
    }

    @Override
    public AuthResponse getCurrentUser(String username) {
        return null;
    }

    @Override
    public void logoutUser(String username) {

    }

    @Override
    public AuthResponse refreshToken(RefreshRequest refreshRequest) {
        return null;
    }
}
