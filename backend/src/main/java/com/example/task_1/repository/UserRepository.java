package com.example.task_1.repository;

import com.example.task_1.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // Find user by username (used for authentication)
    Optional<User> findByUsername(String username);

    // Check if username already exists
    boolean existsByUsername(String username);

    // Check if email already exists
    boolean existsByEmail(String email);
}
