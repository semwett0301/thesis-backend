package com.example.repositories;

import com.example.model.entities.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<UserInfo, String> {
    Optional<UserInfo> findByUsername(String username);
}
