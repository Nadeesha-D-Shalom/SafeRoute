package com.example.saferoute.controllers;

import com.example.saferoute.models.User;
import com.example.saferoute.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/users")
@CrossOrigin
public class UserController {

    @Autowired
    private UserService userService;

    // User Registration API
    @PostMapping("/register")
    public CompletableFuture<String> registerUser(@RequestBody User user) {
        return userService.registerUser(user);
    }

    // User Login API
    @PostMapping("/login")
    public CompletableFuture<String> loginUser(@RequestBody User user) {
        return userService.login(user.getUsername(), user.getPassword());
    }
}

