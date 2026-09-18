package com.example.scalablereadapis.service;

import com.example.scalablereadapis.dto.*;
import com.example.scalablereadapis.exception.PostNotFoundException;
import com.example.scalablereadapis.model.Post;
import com.example.scalablereadapis.repository.PostRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.*;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PostService {
    private static final Logger log = LoggerFactory.getLogger(PostService.class);
    private final PostRepository repository;
    public PostService(PostRepository repository) { this.repository = repository; }

    @Transactional(readOnly = true)
    @Cacheable(value = "posts", key = "#pageable")
    public Page<PostResponse> findPage(Pageable pageable) {
        long started = System.nanoTime();
        Page<PostResponse> result = repository.findAll(pageable).map(PostResponse::from);
        log.info("GET /api/posts page={} size={} took={} ms", pageable.getPageNumber(), pageable.getPageSize(), elapsedMs(started));
        return result;
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "postById", key = "#id")
    public PostResponse findById(Long id) {
        long started = System.nanoTime();
        PostResponse result = PostResponse.from(repository.findById(id).orElseThrow(() -> new PostNotFoundException(id)));
        log.info("GET /api/posts/{} took={} ms", id, elapsedMs(started));
        return result;
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "authorPosts", key = "#author")
    public List<PostResponse> findByAuthor(String author) {
        long started = System.nanoTime();
        List<PostResponse> result = repository.findByAuthor(author).stream().map(PostResponse::from).toList();
        log.info("GET /api/posts/author/{} took={} ms", author, elapsedMs(started));
        return result;
    }

    @Transactional(readOnly = true)
    @Cacheable("postsWithComments")
    public List<PostResponse> findAllWithComments() {
        long started = System.nanoTime();
        List<PostResponse> result = repository.findAllWithComments().stream().map(PostResponse::from).toList();
        log.info("GET /api/posts/with-comments took={} ms (JOIN FETCH)", elapsedMs(started));
        return result;
    }

    @Transactional(readOnly = true)
    @Cacheable("topPosts")
    public List<PostResponse> findTopPosts() {
        long started = System.nanoTime();
        List<PostResponse> result = repository.findTopFiveByLikesNative().stream().map(PostResponse::from).toList();
        log.info("GET /api/posts/top took={} ms (native SQL)", elapsedMs(started));
        return result;
    }

    @Transactional(readOnly = true)
    @Cacheable("analytics")
    public AnalyticsResponse analytics() {
        long started = System.nanoTime();
        long comments = repository.findAllWithComments().stream().mapToLong(post -> post.getComments().size()).sum();
        AnalyticsResponse result = new AnalyticsResponse(repository.count(), repository.sumLikes(), repository.averageLikes(), comments);
        log.info("GET /api/posts/analytics took={} ms (cache miss)", elapsedMs(started));
        return result;
    }

    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "posts", allEntries = true), @CacheEvict(value = "postById", allEntries = true),
            @CacheEvict(value = "authorPosts", allEntries = true), @CacheEvict(value = "postsWithComments", allEntries = true),
            @CacheEvict(value = "topPosts", allEntries = true), @CacheEvict(value = "analytics", allEntries = true)
    })
    public PostResponse create(PostRequest request) {
        Post post = new Post(request.title(), request.content(), request.author(), request.category(), request.likes(), LocalDateTime.now());
        return PostResponse.from(repository.save(post));
    }

    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "posts", allEntries = true), @CacheEvict(value = "postById", allEntries = true),
            @CacheEvict(value = "authorPosts", allEntries = true), @CacheEvict(value = "postsWithComments", allEntries = true),
            @CacheEvict(value = "topPosts", allEntries = true), @CacheEvict(value = "analytics", allEntries = true)
    })
    public PostResponse update(Long id, PostRequest request) {
        Post post = repository.findById(id).orElseThrow(() -> new PostNotFoundException(id));
        post.update(request.title(), request.content(), request.author(), request.category(), request.likes());
        return PostResponse.from(post);
    }

    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "posts", allEntries = true), @CacheEvict(value = "postById", allEntries = true),
            @CacheEvict(value = "authorPosts", allEntries = true), @CacheEvict(value = "postsWithComments", allEntries = true),
            @CacheEvict(value = "topPosts", allEntries = true), @CacheEvict(value = "analytics", allEntries = true)
    })
    public void delete(Long id) {
        if (!repository.existsById(id)) throw new PostNotFoundException(id);
        repository.deleteById(id);
    }

    private long elapsedMs(long started) { return (System.nanoTime() - started) / 1_000_000; }
}
