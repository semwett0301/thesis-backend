package com.example.model.entities.redis;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.redis.core.RedisHash;

@EqualsAndHashCode(callSuper = true)
@RedisHash("RefreshToken")
public class RefreshToken extends BaseToken {
    public RefreshToken(String token, String username, Boolean isActive) {
        super(token, username, isActive);
    }
}
