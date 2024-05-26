package com.example.api;

import com.example.model.dto.request.AuthRequest;
import com.example.model.dto.request.RefreshRequest;
import com.example.model.dto.response.AuthResponse;
import com.example.model.dto.response.UserResponse;
import com.example.services.AuthService.AuthService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {
    private final AuthService authService;

    @GetMapping("/me")
    public UserResponse getMe(@AuthenticationPrincipal Optional<String> username) {
        return authService.getUserByUsername(username.orElse(""));
    }

    @PostMapping("/login")
    public AuthResponse postLogin(@Valid @RequestBody AuthRequest authRequest, @RequestHeader(name = "X-Finger-Print", required = true) String fingerPrint) {
        return authService.loginUser(authRequest, fingerPrint);
    }

    @PostMapping("/logout")
    public void postLogout(@AuthenticationPrincipal Optional<String> usernameOptional, @RequestHeader(name = "X-Finger-Print", required = true) String fingerPrint) {
        usernameOptional.ifPresent(username -> authService.logoutUser(username, fingerPrint));
    }

    @PostMapping("/register")
    public AuthResponse postRegister(@Valid @RequestBody AuthRequest authRequest, @RequestHeader(name = "X-Finger-Print", required = true) String fingerPrint) {
        return authService.registerUser(authRequest, fingerPrint);
    }

    @PostMapping("/refresh")
    public AuthResponse postRefresh(@Valid @RequestBody RefreshRequest refreshRequest, @RequestHeader(name = "X-Finger-Print", required = true) String fingerPrint) {
        return authService.refreshToken(refreshRequest, fingerPrint);
    }
}
