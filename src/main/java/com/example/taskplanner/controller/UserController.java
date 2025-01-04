package com.example.taskplanner.controller;

import com.example.taskplanner.model.User;
import com.example.taskplanner.repository.UserRepository;
import com.example.taskplanner.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }



    @GetMapping("/login")
    public String showLoginForm() {
        return "pages/login";
    }

    @GetMapping("/register")
    public String showRegistrationForm() {
        return "pages/register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user) {
        userService.registerUser(user);
        return "redirect:/login";
    }


}
