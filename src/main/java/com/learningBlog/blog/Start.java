package com.learningBlog.blog;

import com.learningBlog.blog.Post.PostRepository;
import com.learningBlog.blog.User.Role;
import com.learningBlog.blog.User.RoleRepository;
import com.learningBlog.blog.User.User;
import com.learningBlog.blog.User.UserRepository;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class Start {
    public Start(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder){

        User user = new User();
        user.setName("Test");
        user.setEmail("test");
        user.setPassword(passwordEncoder.encode("test"));
        userRepository.save(user);
    }
}