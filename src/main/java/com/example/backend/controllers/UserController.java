package com.example.backend.controllers;

import org.springframework.web.bind.annotation.RestController;
import com.example.backend.entity.User;
import com.example.backend.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;



@RestController
@RequestMapping("/api")
public class UserController {

    private UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @PostMapping("/newuser")
    public ResponseEntity<?> createUser(@RequestParam String username, @RequestParam String password) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password)); // Hashing the password
        userRepository.save(user);
        return ResponseEntity.ok("User created successfully.");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String username, @RequestParam String password) {
        User user = userRepository.findByUsername(username);
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            // Implement session handling here
            return ResponseEntity.ok("Logged in successfully.");
        }
        return ResponseEntity.status(401).body("Invalid username or password.");
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestParam String username) {
        // Implement session invalidation
        return ResponseEntity.ok("Logged out successfully.");
    }

    @DeleteMapping("/logout")
    public ResponseEntity<?> deleteAccount(@RequestParam String username, @RequestParam String password) {
        User user = userRepository.findByUsername(username);
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            userRepository.delete(user);
            return ResponseEntity.ok("Account deleted successfully.");
        }
        return ResponseEntity.status(401).body("Invalid password.");
    }
}
