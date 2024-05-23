package com.example.model.entities.redis;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

@EqualsAndHashCode(callSuper = true)
@RedisHash("AccessToken")
public class AccessToken extends BaseToken {
    public AccessToken(String token, String username, Boolean isActive) {
        super(token, username, isActive);
    }
}
