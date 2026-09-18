package com.example.scalablereadapis.web;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.scalablereadapis.dto.AnalyticsResponse;
import com.example.scalablereadapis.dto.PageResponse;
import com.example.scalablereadapis.dto.PostRequest;
import com.example.scalablereadapis.dto.PostResponse;
import com.example.scalablereadapis.service.PostService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/posts")
public class PostController {
    private final PostService service;
    public PostController(PostService service) { this.service = service; }

    @GetMapping public PageResponse<PostResponse> getPosts(Pageable pageable) { return PageResponse.from(service.findPage(pageable)); }
    @GetMapping("/analytics") public AnalyticsResponse analytics() { return service.analytics(); }
    @GetMapping("/top") public List<PostResponse> top() { return service.findTopPosts(); }
    @GetMapping("/with-comments") public List<PostResponse> withComments() { return service.findAllWithComments(); }
    @GetMapping("/author/{author}") public List<PostResponse> byAuthor(@PathVariable String author) { return service.findByAuthor(author); }
    @GetMapping("/{id}") public PostResponse getPost(@PathVariable Long id) { return service.findById(id); }
    @PostMapping public ResponseEntity<PostResponse> create(@Valid @RequestBody PostRequest request) { return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request)); }
    @PutMapping("/{id}") public PostResponse update(@PathVariable Long id, @Valid @RequestBody PostRequest request) { return service.update(id, request); }
    @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) public void delete(@PathVariable Long id) { service.delete(id); }
}
