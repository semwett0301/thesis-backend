package com.example.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "user_info")
public class UserInfo {
    @Id
    private String username;

    @NotNull
    private String password;
}
