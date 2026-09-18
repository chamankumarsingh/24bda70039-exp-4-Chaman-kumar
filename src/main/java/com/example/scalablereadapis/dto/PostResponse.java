package com.example.scalablereadapis.dto;

import com.example.scalablereadapis.model.Post;
import java.time.LocalDateTime;

public record PostResponse(Long id, String title, String content, String author, String category,
                           Integer likes, LocalDateTime createdAt, int commentCount) {
    public static PostResponse from(Post post) {
        return new PostResponse(post.getId(), post.getTitle(), post.getContent(), post.getAuthor(),
                post.getCategory(), post.getLikes(), post.getCreatedAt(), post.getComments().size());
    }
}
