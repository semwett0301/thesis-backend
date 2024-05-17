package com.example.api;

import com.example.model.dto.request.AuthRequest;
import com.example.model.dto.request.RefreshRequest;
import com.example.model.dto.response.AuthResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @GetMapping("/me")
    public AuthResponse getMe() {
        return null;
    }

    @PostMapping("/login")
    public AuthResponse postLogin(@Valid @RequestBody AuthRequest authRequest) {
        return null;
    }

    @PostMapping("/register")
    public AuthResponse postRegister(@Valid @RequestBody AuthRequest authRequest) {
        return null;
    }

    @PostMapping("/refresh")
    public AuthResponse postRefresh(@Valid @RequestBody RefreshRequest authRequest) {
        return null;
    }
}
