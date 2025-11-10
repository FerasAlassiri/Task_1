package com.example.task_1.dto;

import jakarta.validation.constraints.*;

public record UserCreateRequest(
        @NotBlank @Size(max = 50) String username,
        @NotBlank @Size(min = 8, max = 100) String password,
        @Email @NotBlank String email,
        @NotBlank String mobileNumber
) {}