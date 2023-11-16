package com.learningBlog.blog.Post;

import com.learningBlog.blog.User.Role;
import com.learningBlog.blog.User.RoleRepository;
import com.learningBlog.blog.User.User;
import com.learningBlog.blog.User.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
public class PostController {
    private final UserRepository userRepository;
    private PostRepository postRepository;
    private CommentRepository commentRepository;
    private RoleRepository roleRepository;

    public PostController(PostRepository postRepository, UserRepository userRepository, CommentRepository commentRepository, RoleRepository roleRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
        this.roleRepository = roleRepository;
    }


    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(authentication.getName());
        return user;
    }

    @GetMapping("/")
    public String home(ModelMap model) {
        List<Post> posts = postRepository.findAllByOrderByDateDesc();
        model.addAttribute("posts", posts);
        return "home";
    }

    @GetMapping("/add-post")
    public String showPostForm(ModelMap model) {
        return "addPost";
    }

    @PostMapping("/save-post")
    public String savePost(@RequestParam("postId") String strPostId){
        long postId = Long.parseLong(strPostId);
        Optional<Post> postOptional = postRepository.findById(postId);
        User user = getCurrentUser();
        if(user != null && postOptional.isPresent()) {
            Post post = postOptional.get();
            user.addSavedPost(post);
            userRepository.save(user);
            return "redirect:/show-post/"+postId;
        }
        else return "error";
    }
    @PostMapping("/delete-saved-post")
    public String deleteSavedPost(@RequestParam("postId") String strPostId){
        long postId = Long.parseLong(strPostId);
        Optional<Post> postOptional = postRepository.findById(postId);
        User user = getCurrentUser();
        if(user != null && postOptional.isPresent()) {
            Post post = postOptional.get();
            user.removeSavedPost(post);
            userRepository.save(user);
            return "redirect:/show-post/"+postId;
        }
        else return "error";
    }





    @PostMapping("/add-post")
    public String addPost(ModelMap model, @RequestParam String title, @RequestParam String description) {
        User user = getCurrentUser();
        Post post = new Post(title, description, LocalDateTime.now());
        post.setUser(user);

        postRepository.save(post);
        return "redirect:/";
    }

    @GetMapping("/show-post/{postId}")
    public String showPost(@PathVariable long postId, ModelMap model) {
        Optional<Post> postOptional = postRepository.findById(postId);

        if (postOptional.isPresent()) {
            Post post = postOptional.get();
            model.addAttribute(post);
            model.addAttribute("currentUser", getCurrentUser());

            List<Comment> comments = commentRepository.findByPostOrderByLocalDateTimeDesc(post);
            model.addAttribute("comments", comments);

            return "showPost";
        } else return "postNotFound";

    }


    @GetMapping("/delete-post/{postId}")
    public String deletePost(@PathVariable long postId, ModelMap model) {
        Optional<Post> postOptional = postRepository.findById(postId);
        if (postOptional.isPresent()) {
            Post post = postOptional.get();
            if (post.getUser().getId() == getCurrentUser().getId()) {
                postRepository.delete(post);
                return "redirect:/";
            }else if(roleRepository.findByName("ROLE_MODERATOR").getUsers().contains(getCurrentUser())||roleRepository.findByName("ROLE_ADMIN").getUsers().contains(getCurrentUser())){
                postRepository.delete(post);
                return "redirect:/";
            }

        } else return "postNotFound";
        return "postNotFound";
    }

    @GetMapping("/update-post/{postId}")
    public String showUpdatePost(ModelMap model, @PathVariable long postId) {
        Optional<Post> postOptional = postRepository.findById(postId);

        if (postOptional.isPresent()) {
            Post post = postOptional.get();
            model.addAttribute(post);
            model.addAttribute("currentUser", getCurrentUser());
            return "edit";
        } else return "postNotFound";

    }
    @GetMapping("/error")
    public String error(){
        return "error";
    }


    @PostMapping("/update-post/{postId}")
    public String updatePost(ModelMap model, @PathVariable long postId, @RequestParam String title, @RequestParam String description) {
        User user = getCurrentUser();
        Optional<Post> postOptional = postRepository.findById(postId);
        if(postOptional.isPresent()){
        Post post = postOptional.get();
        post.setUser(user); // Set the user for the post
        post.setLocalDateTime(LocalDateTime.now());
        post.setTitle(title);
        post.setDescription(description);
        postRepository.save(post);

        return "redirect:/";
        }else return "postNotFound";
    }

    @PostMapping("/add-comment/{postId}")
    public String addPost(ModelMap model, @PathVariable long postId, @RequestParam String content) {
        Optional<Post> postOptional = postRepository.findById(postId);
        if (postOptional != null) {
            Post post = postOptional.get();
            Comment comment = new Comment();
            comment.setContent(content);
            comment.setLocalDateTime(LocalDateTime.now());
            comment.setUser(getCurrentUser());
            post.addComment(comment);
            postRepository.save(post);
        }
        return "redirect:/show-post/" + postId;
    }

    @GetMapping("/delete-comment/{commentId}")
    public String deleteComment(@PathVariable long commentId, ModelMap model) {
        long postId;
        Optional<Comment> commentOptional = commentRepository.findById(commentId);
        if (commentOptional.isPresent()) {

            Comment comment = commentOptional.get();
            postId = comment.getPost().getId();
            if (comment.getUser().getId() == getCurrentUser().getId()) {
                commentRepository.delete(comment);
                return "redirect:/show-post/" + postId;
            } else if (roleRepository.findByName("ROLE_MODERATOR").getUsers().contains(getCurrentUser()) || roleRepository.findByName("ROLE_ADMIN").getUsers().contains(getCurrentUser())) {
                commentRepository.delete(comment);
                return "redirect:/show-post/" + postId;
            } else return "postNotFound";


        }
        return "error";
    }
}
