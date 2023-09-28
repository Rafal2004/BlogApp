package com.learningBlog.blog.User;

import java.security.Principal;

public class CustomPrincipal implements Principal {
    private String name;
    private String id; // You can add an ID field

    public CustomPrincipal(String name, String id) {
        this.name = name;
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    // Custom method to get the ID
    public String getId() {
        return id;
    }
}