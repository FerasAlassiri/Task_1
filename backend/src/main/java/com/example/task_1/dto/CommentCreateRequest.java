package com.example.task_1.dto;
import jakarta.validation.constraints.*;
public record CommentCreateRequest(@NotBlank @Size(max=100) String text) {}