package com.example.model.dto.response;

import com.auth0.jwt.JWT;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AuthResponse {
    private String access;
    private String refresh;
}
