package com.learningBlog.blog.User;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private List<Role> roles =new ArrayList<>();

}
