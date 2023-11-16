package com.learningBlog.blog.User;


import com.learningBlog.blog.Post.Post;
import com.learningBlog.blog.Post.PostRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(path = "/user")
public class UserController {
    private UserRepository userRepository;
    private PostRepository postRepository;

    private RoleRepository roleRepository;

    private UserService userService;

    private CustomUserDetailsService customUserDetailsService;

    private User getCurrentUser(){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(authentication.getName());
        System.out.println(authentication.getName());
        return user;
    }

    public UserController(UserRepository userRepository, PostRepository postRepository
            , CustomUserDetailsService customUserDetailsService, RoleRepository roleRepository, UserService userService) {
        super();
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.customUserDetailsService=customUserDetailsService;
        this.roleRepository = roleRepository;
        this.userService = userService;
    }

    @GetMapping("/settings")
    public String showUserDetails(ModelMap model){
        Optional<User> userOptional = userRepository.findById(getCurrentUser().getId());

        if(userOptional.isPresent()){
            User user = userOptional.get();
            model.addAttribute("user",user);
            return "showUser";
        }
        return "error";
    }

    @GetMapping("/edit")
    public String showEditUser(ModelMap model){
        Optional<User> userOptional = userRepository.findById(getCurrentUser().getId());
        if(userOptional.isPresent()){
            User user = userOptional.get();
//            System.out.println(user.toString());
            model.addAttribute("user",user);
            return "editUser";
        }
        return "error";
    }

    @PostMapping("/edit")
    public String EditUser(ModelMap model, @RequestParam String name, @RequestParam String lastname, @RequestParam String email){
        User currentUser = getCurrentUser();
        if (currentUser != null) {

            Optional<User> userOptional = userRepository.findById(currentUser.getId());
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                user.setName(name);
                user.setLastname(lastname);
                user.setEmail(email);
                userRepository.save(user);
                Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                Authentication newAuth = new UsernamePasswordAuthenticationToken(email, auth.getCredentials(), auth.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(newAuth);

                return "redirect:/";
            }
        }
        // Handle the case when the user is not logged in or not found
        return "redirect:/login"; // R

}

    @GetMapping("/saved-posts")
    public String showUserPage(ModelMap model){

            User user = getCurrentUser();
        List<Post> posts = userService.getSavedPosts(user);
        model.addAttribute("posts", posts);

        model.addAttribute("user", user);

            return "userPage";
        }



    @GetMapping("/change-password")
    public String showChangePasswordForm(){
        return "changePassword";
    }

    @PostMapping("/change-password")
    public String changePassword(@RequestParam("oldPassword") String oldPassword,
                                 @RequestParam("newPassword") String newPassword,
                                 Principal principal) {
        // Get the currently authenticated user
        String username = principal.getName();

        // Check if the old password matches
        if (!customUserDetailsService.isOldPasswordValid(username, oldPassword)) {
            // Handle invalid old password, e.g., show an error message
            return "redirect:/user/change-password?error";
        }

        // Change the password
        customUserDetailsService.changePassword(username, newPassword);

        // Redirect to a success page or display a success message
        return "redirect:/user/change-password?success";
    }
    @PostMapping("/add-role")
    public String addRole(@RequestParam("roleId") String strRoleId, @RequestParam("userId") String strUserId){

        long roleId = Long.parseLong(strRoleId);
        long userId = Long.parseLong(strUserId);
        Optional<Role> roleOptional = roleRepository.findById(roleId);
        Optional<User> userOptional = userRepository.findById(userId);

        if(userOptional.isPresent() && roleOptional.isPresent()) {
            User user = userOptional.get();
            Role role = roleOptional.get();

            //Checking whether the moderator wants to give someone admin. ONLY ADMIN SHOULD ADD ANOTHER ADMIN
            if(role.getName().equals("ROLE_ADMIN")){
                for(Role roleCurrentUser : getCurrentUser().getRoles()){
                    if(roleCurrentUser.getName().equals("ROLE_ADMIN")){
                        user.addRole(role);
                        userRepository.save(user);
                        return "redirect:/user/all";
                    }
                }
                return "error";
            }
            user.addRole(role);
            userRepository.save(user);
            return "redirect:/user/all";
        }
        else return "error";
        }
    @PostMapping("/delete-role")
    public String deleteRole(@RequestParam("roleId") String strRoleId, @RequestParam("userId") String strUserId){

        long roleId = Long.parseLong(strRoleId);
        long userId = Long.parseLong(strUserId);
        Optional<Role> roleOptional = roleRepository.findById(roleId);
        Optional<User> userOptional = userRepository.findById(userId);


        //Checking whether the moderator wants to delete admin. ONLY ADMIN SHOULD DELETE OTHER ADMIN
        if(userOptional.isPresent() && roleOptional.isPresent()) {
            User user = userOptional.get();
            Role role = roleOptional.get();
            if(role.getName().equals("ROLE_ADMIN")){
                for(Role roleCurrentUser : getCurrentUser().getRoles()){
                    if(roleCurrentUser.getName().equals("ROLE_ADMIN")){
                        user.removeRole(role);
                        userRepository.save(user);
                        return "redirect:/user/all";
                    }
                }
                return "error";
            }
            user.removeRole(role);
            userRepository.save(user);
            return "redirect:/user/all";
        }
        else return "error";
    }


    @PostMapping("/delete-user")
    public String deleteUser(@RequestParam("userId") String strUserId){
        long id = Long.parseLong(strUserId);

        Role role = roleRepository.findByName("ROLE_ADMIN");
        Optional<User> userOptional = userRepository.findById(id);
        if(userOptional.isPresent()){
            User user = userOptional.get();
            if(!getCurrentUser().getRoles().contains(role)){
                System.out.println("SDADSA");
                if(user.getRoles().contains(role)){
                    return "error";
                }}
            userRepository.delete(user);
            return "redirect:/user/all";
        }
        return "error";

    }

    @GetMapping("/all")
    public String all_users(Model model) {
        List<User> users = userRepository.findAll();
        model.addAttribute("users", users);
        model.addAttribute("currentUser", getCurrentUser());
        return "users";
    }


}



