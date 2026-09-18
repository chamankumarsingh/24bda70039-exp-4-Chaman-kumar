package com.example.scalablereadapis.model;

import jakarta.persistence.*;

@Entity
@Table(name = "comments")
public class Comment {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 120) private String author;
    @Column(nullable = false, length = 1000) private String text;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;
    protected Comment() { }
    public Comment(String author, String text, Post post) { this.author = author; this.text = text; this.post = post; }
    public Long getId() { return id; }
    public String getAuthor() { return author; }
    public String getText() { return text; }
}
