package com.example.scalablereadapis.config;

import com.example.scalablereadapis.model.*;
import com.example.scalablereadapis.repository.PostRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.time.LocalDateTime;

@Configuration
public class SampleDataConfig {
    @Bean
    CommandLineRunner seedPosts(PostRepository repository) {
        return args -> {
            if (repository.count() > 0) return;
            String[] authors = {"John", "Priya", "Alex", "Maria", "John"};
            String[] categories = {"Technology", "Travel", "Analytics", "Books", "Food"};
            String[] topics = {"Building fast APIs", "Caching strategies", "Useful database indexes", "Readable Java design", "Measuring throughput", "Pagination patterns", "Spring Boot tips", "H2 query tuning", "Dashboard architecture", "Reliable validation", "Efficient sorting", "Clean API errors", "JPA query planning", "Scaling post feeds", "Performance lessons"};
            for (int i = 0; i < topics.length; i++) {
                Post post = new Post(topics[i], "A practical example of " + topics[i].toLowerCase() + " for a scalable read API.", authors[i % authors.length], categories[i % categories.length], 15 + (i * 13) % 180, LocalDateTime.now().minusDays(15 - i));
                post.getComments().add(new Comment("Reader" + (i + 1), "Useful practical example.", post));
                if (i % 3 == 0) post.getComments().add(new Comment("Student", "This is easy to test.", post));
                repository.save(post);
            }
        };
    }
}
