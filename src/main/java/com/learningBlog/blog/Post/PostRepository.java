package com.learningBlog.blog.Post;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long>{
    public List<Post> findByUserId(long userId);
    @Query("SELECT p FROM Post p ORDER BY p.localDateTime DESC")
    public List<Post> findAllByOrderByDateDesc();
    }

