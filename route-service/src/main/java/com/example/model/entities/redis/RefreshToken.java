package com.example.model.entities.redis;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.redis.core.RedisHash;

@EqualsAndHashCode(callSuper = true)
@RedisHash("RefreshToken")
@Getter
@Setter
public class RefreshToken extends BaseToken {
    private String fingerPrint;

    public RefreshToken(String token, String username, String fingerPrint, Boolean isActive) {
        super(token, username, isActive);
        this.fingerPrint = fingerPrint;
    }
}
