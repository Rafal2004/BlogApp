package com.learningBlog.blog.AuthController;

import com.learningBlog.blog.User.User;
import com.learningBlog.blog.User.UserRepository;
import com.learningBlog.blog.User.UserService;
import jakarta.validation.Valid;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
public class AuthController {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private UserService userService;
    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder, UserService userService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
    }


    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "register";
    }


    @PostMapping("register/save")
    public String registration(@Valid @ModelAttribute("user") User tempUser,
                               BindingResult result,
                               Model model) {
        User existingUser = userRepository.findUserByEmail(tempUser.getEmail());

        if (existingUser != null && existingUser.getEmail() != null) {
            result.rejectValue("email", null,
                    "There is already an account registered with the same email");
        }
        if (result.hasErrors()) {
            System.out.println(result.toString());
            model.addAttribute("user", tempUser);
            return "register";
        }
        User user = userService.hashPasswordAndSetRole(tempUser);

        userRepository.save(user);
        return "redirect:/register?success";
    }



}
