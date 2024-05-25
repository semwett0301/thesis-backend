package com.example.model.entities.redis;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@RedisHash("AccessToken")
@Getter
@Setter
@AllArgsConstructor
public class AccessToken implements BaseToken {
    @Id
    private final String token;

    @Indexed
    private String username;

    private Boolean isActive;
}
