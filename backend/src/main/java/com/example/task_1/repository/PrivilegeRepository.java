package com.example.task_1.repository;

import com.example.task_1.entity.Privilege;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PrivilegeRepository extends JpaRepository<Privilege, Long> {

    // Find privilege by its unique name (e.g., "USER", "ADMIN")
    Optional<Privilege> findByName(String name);
}
