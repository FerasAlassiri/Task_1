package com.example.task_1.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.*;
import java.util.*;

@Entity
@Getter @Setter @NoArgsConstructor
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank @Size(max = 100)
    private String title;

    @NotBlank @Size(max = 500)
    @Column(length = 500)
    private String body;

    @NotBlank
    private String author; // username of creator

    private LocalDateTime createdAt = LocalDateTime.now();

    @Lob // large image stored as binary
    private byte[] image;

    private int likesCount = 0;
    private int dislikesCount = 0;

    private boolean disabled = false; // soft delete or moderation flag

    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>(); // linked comments
}