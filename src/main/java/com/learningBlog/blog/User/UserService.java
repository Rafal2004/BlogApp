package com.learningBlog.blog.User;

import com.learningBlog.blog.Post.Post;

import java.util.List;

public interface UserService {

    User hashPasswordAndSetRole(User user);
    List<Post> getSavedPosts(User user);


}