package com.example.task_1.controller;

import com.example.task_1.dto.*;
import com.example.task_1.entity.User;
import com.example.task_1.service.UserService;
import com.example.task_1.util.Mappers;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:5173") // local dev CORS (omit if using global CORS)
public class UserController {

    private final UserService users;
    public UserController(UserService users) { this.users = users; }

    @PostMapping(value = "/user", consumes = "application/json", produces = "application/json")
    public ResponseEntity<UserResponse> register(@Validated @RequestBody UserCreateRequest req) {
        User saved = users.register(req);
        return ResponseEntity.status(201).body(Mappers.toUserResponse(saved)); // 201 Created
    }

    @GetMapping("/login") // returns current principal and authorities
    public ResponseEntity<LoginResponse> login(Authentication auth) {
        String[] auths = auth == null ? new String[0] :
                auth.getAuthorities().stream().map(a -> a.getAuthority()).toArray(String[]::new);
        String name = auth == null ? null : auth.getName();
        return ResponseEntity.ok(new LoginResponse(name, auths));
    }

    @GetMapping("/logout") // Basic Auth: client just forgets credentials
    public ResponseEntity<?> logout() {
        return ResponseEntity.ok().build();
    }
}
