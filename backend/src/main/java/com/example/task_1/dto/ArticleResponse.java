package com.example.task_1.dto;
import java.time.*;
public record ArticleResponse(
Long id, String title, String body, String author,
LocalDateTime createdAt, Integer likes, Integer dislikes, Boolean disabled
) {}