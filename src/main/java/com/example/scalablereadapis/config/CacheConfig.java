package com.example.scalablereadapis.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.*;
import java.time.Duration;

@Configuration
public class CacheConfig {
    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager manager = new CaffeineCacheManager("posts", "postById", "authorPosts", "postsWithComments", "topPosts", "analytics");
        manager.setCaffeine(Caffeine.newBuilder().maximumSize(500).expireAfterWrite(Duration.ofMinutes(10)));
        return manager;
    }
}
