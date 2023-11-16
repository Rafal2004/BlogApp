package com.learningBlog.blog.User;

import com.learningBlog.blog.Post.Post;
import com.learningBlog.blog.Post.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PostRepository postRepository;
    private PasswordEncoder passwordEncoder;


    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, PostRepository postRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }
//    @Override
//    public void saveUser(User tempUser) {
//        User user = new User();
//        user.setName(userDto.getFirstName());
//        user.setLastname(userDto.getLastName());
//        user.setEmail(userDto.getEmail());
//        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
//
//
//        Role role = roleRepository.findByName("ROLE_USER");
//        if(role ==null)
//
//        {
//            role = checkRoleExist();
//        }
//        user.setRoles(Arrays.asList(role));
//        userRepository.save(user);
//    }
//
//
//
//    private Role checkRoleExist(){
//        Role role = new Role();
//        role.setName("ROLE_USER");
//        return roleRepository.save(role);
//    }

    @Override
    public User hashPasswordAndSetRole(User tempUser) {
        User user = new User();
        user.setName(tempUser.getName());
        user.setLastname(tempUser.getLastname());
        user.setEmail(tempUser.getEmail());
        user.setPassword(passwordEncoder.encode(tempUser.getPassword()));


        Role role = roleRepository.findByName("ROLE_USER");
        user.addRole(role);
        return user;
    }

    @Override
    public List<Post> getSavedPosts(User user) {
    List<Post> posts = new ArrayList<>(user.getSavedPosts());
    return posts;
    }
}