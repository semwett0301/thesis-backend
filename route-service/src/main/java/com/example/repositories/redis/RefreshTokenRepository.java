package com.example.repositories.redis;

import com.example.model.entities.redis.RefreshToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {
    List<RefreshToken> findByUsername(String username);
}
