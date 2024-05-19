package com.example.api;

import com.example.model.dto.request.AuthRequest;
import com.example.model.dto.response.AuthResponse;
import com.example.services.AuthService.AuthService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {
    private final AuthService authService;

    @GetMapping("/me")
    public AuthResponse getMe() {
        return authService.getCurrentUser("");
    }

    @PostMapping("/login")
    public AuthResponse postLogin(@Valid @RequestBody AuthRequest authRequest) {
        return authService.loginUser(authRequest);
    }

    @PostMapping("/logout")
    public void postLogout() {
        authService.logoutUser("");
    }

    @PostMapping("/register")
    public AuthResponse postRegister(@Valid @RequestBody AuthRequest authRequest) {
        return authService.registerUser(authRequest);
    }

    @PostMapping("/refresh")
    public AuthResponse postRefresh(@Valid @RequestBody AuthRequest refreshRequest) {
        return authService.refreshToken(refreshRequest);
    }
}
