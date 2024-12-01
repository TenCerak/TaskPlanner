package com.example.taskplanner.controller;

import com.example.taskplanner.model.User;
import com.example.taskplanner.repository.UserRepository;
import com.example.taskplanner.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping()
    public List<User> list() {
        return userRepository.findAll();
    }

    @GetMapping("/{id}")
    public User detail(@PathVariable Long id) {
        return userRepository.findById(id).orElse(null);
    }



}
