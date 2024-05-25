package com.example.model.entities.redis;

import java.io.Serializable;

public interface BaseToken extends Serializable {
    void setIsActive(Boolean bool);
}
