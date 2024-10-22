package com.example.backend.controllers;

import com.example.backend.entity.User;
import com.example.backend.repository.UserRepository;
import com.example.backend.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private final UserService userService;

    public UserController(UserRepository userRepository, PasswordEncoder passwordEncoder, UserService userService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
    }

    // Create new user
    @PostMapping("/newuser")
    public ResponseEntity<User> createUser(@RequestParam String username, @RequestParam String password) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        User createdUser = userService.createUser(user);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    // Log in user
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String username, @RequestParam String password, HttpSession session) {
        if (userService.authenticate(username, password)) {
            session.setAttribute("username", username);
            return new ResponseEntity<>("Login successful!", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Invalid username or password.", HttpStatus.UNAUTHORIZED);
        }
    }

    // Log out user
    @PostMapping("/logout")
    @GetMapping("/logout") // Allow both POST and GET requests
    public ResponseEntity<String> logout(@RequestParam String username, HttpSession session) {
        // Invalidate the session regardless of the username provided
        session.invalidate();
        return ResponseEntity.ok(username + " logged out successfully!");
    }

    @GetMapping("/isLoggedIn")
    public ResponseEntity<String> isLoggedIn(HttpSession session) {
        String loggedInUsername = (String) session.getAttribute("username");

        if (loggedInUsername != null) {
            return ResponseEntity.ok("User " + loggedInUsername + " is logged in.");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No user is currently logged in.");
        }
    }

    @DeleteMapping("/logout")
    public ResponseEntity<String> deleteAccount(
            @RequestParam String username,
            HttpSession session) {

        // Check if the user is logged in and the username matches
        String loggedInUsername = (String) session.getAttribute("username");
        if (!loggedInUsername.equals(username)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("You are not authorized to delete this account.");
        }

        // Fetch the user from the repository
        User user = userService.findByUsername(username);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("User not found.");
        }

        // Delete the user account
        userRepository.deleteByUsername(username);

        // Invalidate the session after deletion
        session.invalidate();

        return ResponseEntity.ok("Account successfully deleted.");
    }

    @GetMapping("{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(user);
    }

}