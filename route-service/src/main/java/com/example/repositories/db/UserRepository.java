package com.example.repositories.db;

import com.example.model.entities.db.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserInfo, String> {
    Optional<UserInfo> findByUsername(String username);
}
