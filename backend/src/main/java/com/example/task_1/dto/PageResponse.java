package com.example.task_1.dto;
public record PageResponse<T>(int page, int size, long totalElements, int totalPages, java.util.List<T> content) {}