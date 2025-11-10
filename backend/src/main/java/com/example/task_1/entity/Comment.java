package com.example.task_1.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.*;

@Entity
@Getter @Setter @NoArgsConstructor
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false) // each comment belongs to one article
    private Article article;

    @NotBlank @Size(max = 100)
    private String text;

    private LocalDateTime createdAt = LocalDateTime.now();

    @NotBlank
    private String username; // comment author's username
}
