package com.shop.ecommerce.controller;

import com.shop.ecommerce.entity.User;
import com.shop.ecommerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping(value = "/test", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> test() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Auth controller is working!");
        response.put("success", true);
        response.put("timestamp", LocalDateTime.now().toString());
        System.out.println("✅ Test endpoint called successfully");
        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "/login",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, Object> request) {
        Map<String, Object> response = new HashMap<>();

        try {
            String email = (String) request.get("email");
            String password = (String) request.get("password");

            System.out.println("🔐 Login attempt for email: " + email);

            // Basic validation
            if (email == null || email.trim().isEmpty()) {
                response.put("message", "Email is required");
                response.put("success", false);
                return ResponseEntity.badRequest().body(response);
            }

            if (password == null || password.trim().isEmpty()) {
                response.put("message", "Password is required");
                response.put("success", false);
                return ResponseEntity.badRequest().body(response);
            }

            // Find user by email
            Optional<User> userOptional = userRepository.findByEmail(email.trim().toLowerCase());

            if (userOptional.isPresent()) {
                User user = userOptional.get();
                if (user.getPassword().equals(password)) {
                    System.out.println("✅ Login successful for: " + user.getFirstName());
                    response.put("message", "Login successful");
                    response.put("success", true);
                    response.put("userId", user.getUserId());
                    response.put("firstName", user.getFirstName());
                    response.put("lastName", user.getLastName());
                    response.put("email", user.getEmail());
                    return ResponseEntity.ok(response);
                } else {
                    System.out.println("❌ Invalid password for: " + email);
                    response.put("message", "Invalid email or password");
                    response.put("success", false);
                    return ResponseEntity.ok(response);
                }
            } else {
                System.out.println("❌ User not found: " + email);
                response.put("message", "Invalid email or password");
                response.put("success", false);
                return ResponseEntity.ok(response);
            }
        } catch (Exception e) {
            System.err.println("❌ Login error: " + e.getMessage());
            e.printStackTrace();
            response.put("message", "Login failed: " + e.getMessage());
            response.put("success", false);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping(value = "/register",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> register(@RequestBody Map<String, Object> request) {
        Map<String, Object> response = new HashMap<>();

        try {
            String firstName = (String) request.get("firstName");
            String lastName = (String) request.get("lastName");
            String email = (String) request.get("email");
            String password = (String) request.get("password");
            String phoneNumber = (String) request.get("phoneNumber");

            System.out.println("📝 Registration attempt for: " + email);
            System.out.println("📝 Request data: " + firstName + " " + lastName);

            // Validation
            if (firstName == null || firstName.trim().isEmpty()) {
                response.put("message", "First name is required");
                response.put("success", false);
                return ResponseEntity.badRequest().body(response);
            }

            if (lastName == null || lastName.trim().isEmpty()) {
                response.put("message", "Last name is required");
                response.put("success", false);
                return ResponseEntity.badRequest().body(response);
            }

            if (email == null || email.trim().isEmpty()) {
                response.put("message", "Email is required");
                response.put("success", false);
                return ResponseEntity.badRequest().body(response);
            }

            if (password == null || password.length() < 6) {
                response.put("message", "Password must be at least 6 characters");
                response.put("success", false);
                return ResponseEntity.badRequest().body(response);
            }

            // Check if email already exists
            boolean emailExists = userRepository.existsByEmail(email.trim().toLowerCase());
            if (emailExists) {
                System.out.println("❌ Email already exists: " + email);
                response.put("message", "Email already exists. Please use a different email.");
                response.put("success", false);
                return ResponseEntity.badRequest().body(response);
            }

            // Get next available UserID
            Integer nextUserId = getNextUserId();
            System.out.println("🆔 Next UserID: " + nextUserId);

            // Create new user
            User newUser = new User();
            newUser.setUserId(nextUserId);
            newUser.setFirstName(firstName.trim());
            newUser.setLastName(lastName.trim());
            newUser.setPassword(password);
            newUser.setEmail(email.trim().toLowerCase());
            newUser.setPhoneNumber(phoneNumber);
            newUser.setCreatedAt(LocalDateTime.now());

            // Save user to database
            User savedUser = userRepository.save(newUser);
            System.out.println("✅ User registered successfully: " + savedUser.getUserId());

            response.put("message", "Registration successful! You can now login with your credentials.");
            response.put("success", true);
            response.put("userId", savedUser.getUserId());
            response.put("firstName", savedUser.getFirstName());
            response.put("lastName", savedUser.getLastName());
            response.put("email", savedUser.getEmail());
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            System.err.println("❌ Registration error: " + e.getMessage());
            e.printStackTrace();
            response.put("message", "Registration failed: " + e.getMessage());
            response.put("success", false);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping(value = "/check-email", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> checkEmailAvailability(@RequestParam String email) {
        Map<String, Object> response = new HashMap<>();

        try {
            System.out.println("📧 Email check for: " + email);
            boolean exists = userRepository.existsByEmail(email.trim().toLowerCase());
            response.put("available", !exists);
            response.put("message", exists ? "Email already exists" : "Email is available");
            response.put("success", true);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println("❌ Email check error: " + e.getMessage());
            e.printStackTrace();
            response.put("message", "Error checking email: " + e.getMessage());
            response.put("success", false);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    private Integer getNextUserId() {
        try {
            Integer maxId = userRepository.findAll().stream()
                    .mapToInt(User::getUserId)
                    .max()
                    .orElse(4); // Start from 5 since you have 1-4
            return maxId + 1;
        } catch (Exception e) {
            System.out.println("⚠️ Error getting next UserID, using fallback: " + e.getMessage());
            return 5; // Fallback to 5
        }
    }
}
