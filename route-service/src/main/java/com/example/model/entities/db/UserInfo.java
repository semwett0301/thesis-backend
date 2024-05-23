package com.example.model.entities.db;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
@Table(name = "user_info")
public class UserInfo {
    @Id
    private String username;

    @NotNull
    private String password;
}
