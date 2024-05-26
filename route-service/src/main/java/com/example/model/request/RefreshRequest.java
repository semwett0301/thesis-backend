package com.example.model.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RefreshRequest {
    @NotNull
    private String refresh;
}
