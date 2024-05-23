package com.example.api;

import com.example.model.dto.request.AuthRequest;
import com.example.model.dto.request.RefreshRequest;
import com.example.model.dto.response.AuthResponse;
import com.example.model.dto.response.UserResponse;
import com.example.security.model.AuthenticationToken;
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
    public UserResponse getMe(AuthenticationToken auth) {
        return authService.getUserByUsername(auth.getPrincipal());
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
    public AuthResponse postRefresh(@Valid @RequestBody RefreshRequest refreshRequest) {
        return authService.refreshToken(refreshRequest);
    }
}
