package com.learningBlog.blog.User;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);


    User findUserByEmail(String email);

    User save(User user);


    List<User> findAll();
}
