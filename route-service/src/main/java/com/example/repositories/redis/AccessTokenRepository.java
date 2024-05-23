package com.example.repositories.redis;

import com.example.model.entities.redis.AccessToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccessTokenRepository extends CrudRepository<AccessToken, String> {
    List<AccessToken> findByUsername(String username);
}
