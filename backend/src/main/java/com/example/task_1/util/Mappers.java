package com.example.task_1.util;

import com.example.task_1.dto.*;
import com.example.task_1.entity.*;

public class Mappers {

    // Convert User entity to response DTO
    public static UserResponse toUserResponse(User u) {
        return new UserResponse(u.getId(), u.getUsername(), u.getEmail(), u.getMobileNumber());
    }

    // Convert Article entity to response DTO
    public static ArticleResponse toArticleResponse(Article a) {
        return new ArticleResponse(
            a.getId(),
            a.getTitle(),
            a.getBody(),
            a.getAuthor(),
            a.getCreatedAt(),
            a.getLikesCount(),
            a.getDislikesCount(),
            a.isDisabled()
        );
    }

    // Convert Comment entity to response DTO
    public static CommentResponse toCommentResponse(Comment c) {
        return new CommentResponse(c.getId(), c.getText(), c.getUsername(), c.getCreatedAt());
    }
}
