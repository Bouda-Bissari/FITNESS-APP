package com.feetness.feetness.repositories;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.feetness.feetness.models.User;

public interface UserRepo extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
    Boolean existsByEmail(String email);


}