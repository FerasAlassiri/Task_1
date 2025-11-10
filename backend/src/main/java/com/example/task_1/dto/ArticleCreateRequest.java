package com.example.task_1.dto;
import jakarta.validation.constraints.*;
public record ArticleCreateRequest(
@NotBlank @Size(max=100) String title,
@NotBlank @Size(max=500) String body
) {}