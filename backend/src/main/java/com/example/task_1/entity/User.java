package com.example.task_1.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Entity
@Table(name = "users")
@Getter @Setter @NoArgsConstructor
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 50)
    @NotBlank @Size(max = 50)
    private String username;

    @NotBlank @Size(min = 8, max = 100)
    private String password; // stored as BCrypt hash

    @Column(unique = true, nullable = false)
    @Email @NotBlank
    private String email;

    @NotBlank
    @Pattern(regexp = "^[- +()0-9]{8,20}$")
    private String mobileNumber;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "user_privileges",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "privilege_id")
    )
    private Set<Privilege> privileges = new HashSet<>(); // roles or authorities

    // --- Spring Security UserDetails implementation ---
    @Override public Collection<? extends GrantedAuthority> getAuthorities() { return privileges; }
    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }
}
