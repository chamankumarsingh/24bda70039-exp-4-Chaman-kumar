package com.example.scalablereadapis.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "posts", indexes = {
        @Index(name = "idx_posts_author", columnList = "author"),
        @Index(name = "idx_posts_category", columnList = "category"),
        @Index(name = "idx_posts_created_at", columnList = "created_at")
})
public class Post {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 150) private String title;
    @Column(nullable = false, length = 4000) private String content;
    @Column(nullable = false, length = 80) private String author;
    @Column(nullable = false, length = 80) private String category;
    @Column(nullable = false) private Integer likes = 0;
    @Column(nullable = false) private LocalDateTime createdAt;
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    protected Post() { }
    public Post(String title, String content, String author, String category, Integer likes, LocalDateTime createdAt) {
        this.title = title; this.content = content; this.author = author; this.category = category;
        this.likes = likes; this.createdAt = createdAt;
    }
    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getContent() { return content; }
    public String getAuthor() { return author; }
    public String getCategory() { return category; }
    public Integer getLikes() { return likes; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public List<Comment> getComments() { return comments; }
    public void update(String title, String content, String author, String category, Integer likes) {
        this.title = title; this.content = content; this.author = author; this.category = category; this.likes = likes;
    }
}
