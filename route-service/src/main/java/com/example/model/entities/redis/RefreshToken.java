package com.example.model.entities.redis;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@RedisHash("RefreshToken")
@Getter
@Setter
@AllArgsConstructor
public class RefreshToken implements BaseToken {
    @Id
    private final String token;

    @Indexed
    private String username;

    private Boolean isActive;

    @Indexed
    private String fingerPrint;
}
