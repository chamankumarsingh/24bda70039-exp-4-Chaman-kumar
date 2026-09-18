package com.example.scalablereadapis.repository;

import com.example.scalablereadapis.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    @Query("SELECT p FROM Post p WHERE p.author = :author")
    List<Post> findByAuthor(@Param("author") String author);

    @Query("SELECT DISTINCT p FROM Post p LEFT JOIN FETCH p.comments")
    List<Post> findAllWithComments();

    @Query(value = "SELECT * FROM posts ORDER BY likes DESC LIMIT 5", nativeQuery = true)
    List<Post> findTopFiveByLikesNative();

    @Query("SELECT COALESCE(SUM(p.likes), 0) FROM Post p")
    long sumLikes();

    @Query("SELECT COALESCE(AVG(p.likes), 0) FROM Post p")
    double averageLikes();
}
