package com.example.task_1.service;

import com.example.task_1.dto.UserCreateRequest;
import com.example.task_1.entity.Privilege;
import com.example.task_1.entity.User;
import com.example.task_1.repository.PrivilegeRepository;
import com.example.task_1.repository.UserRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository users;
    private final PrivilegeRepository privileges;
    private final PasswordEncoder encoder;

    public UserService(UserRepository users,
                       PrivilegeRepository privileges,
                       PasswordEncoder encoder) {
        this.users = users;
        this.privileges = privileges;
        this.encoder = encoder;
    }

    @Transactional // register a new user with USER privilege
    public User register(UserCreateRequest req) {
        if (users.existsByUsername(req.username())) throw new IllegalArgumentException("username-exists");
        if (users.existsByEmail(req.email())) throw new IllegalArgumentException("email-exists");

        User u = new User();
        u.setUsername(req.username());
        u.setPassword(encoder.encode(req.password())); // hash password
        u.setEmail(req.email());
        u.setMobileNumber(req.mobileNumber());

        // ensure USER privilege exists
        Privilege userPriv = privileges.findByName("USER").orElseGet(() -> {
            Privilege p = new Privilege(); p.setName("USER"); return privileges.save(p);
        });
        u.getPrivileges().add(userPriv);

        try {
            return users.save(u);
        } catch (DataIntegrityViolationException ex) {
            // unique constraint race (username/email)
            throw new IllegalArgumentException("user-or-email-exists");
        }
    }

    @Override // required by Spring Security
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return users.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("not-found"));
    }
}
