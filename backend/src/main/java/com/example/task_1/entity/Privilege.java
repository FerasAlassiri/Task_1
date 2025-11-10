package com.example.task_1.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;

@Entity
@Getter @Setter @NoArgsConstructor
public class Privilege implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name; // e.g., USER or ADMIN

    @Override
    public String getAuthority() {
        return name; // required by Spring Security
    }
}
