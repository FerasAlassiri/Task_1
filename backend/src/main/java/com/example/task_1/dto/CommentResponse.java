package com.example.task_1.dto;
import java.time.*;
public record CommentResponse(Long id, String text, String username, LocalDateTime createdAt) {}