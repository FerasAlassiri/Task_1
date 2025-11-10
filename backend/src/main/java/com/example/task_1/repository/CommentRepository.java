package com.example.task_1.repository;

import com.example.task_1.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    // Fetch all comments linked to a specific article
    List<Comment> findByArticleId(Long articleId);
}
