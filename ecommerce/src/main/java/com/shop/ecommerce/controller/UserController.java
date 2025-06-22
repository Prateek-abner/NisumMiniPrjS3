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
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping(value = "/test", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> test() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "User controller is working!");
        response.put("success", true);
        response.put("timestamp", LocalDateTime.now().toString());
        System.out.println("✅ User Test endpoint called successfully");
        return ResponseEntity.ok(response);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> getAllUsers() {
        Map<String, Object> response = new HashMap<>();

        try {
            System.out.println("👥 Getting all users");
            List<User> users = userRepository.findAll();

            List<Map<String, Object>> userList = users.stream()
                    .map(this::convertUserToSafeMap)
                    .toList();

            response.put("message", "Users retrieved successfully");
            response.put("success", true);
            response.put("users", userList);
            response.put("count", userList.size());
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            System.err.println("❌ Error getting all users: " + e.getMessage());
            e.printStackTrace();
            response.put("message", "Failed to retrieve users: " + e.getMessage());
            response.put("success", false);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> getUserById(@PathVariable Integer id) {
        Map<String, Object> response = new HashMap<>();

        try {
            System.out.println("👤 Getting user by ID: " + id);
            Optional<User> userOptional = userRepository.findById(id);

            if (userOptional.isPresent()) {
                User user = userOptional.get();
                response.put("message", "User found successfully");
                response.put("success", true);
                response.put("user", convertUserToSafeMap(user));
                return ResponseEntity.ok(response);
            } else {
                response.put("message", "User not found with ID: " + id);
                response.put("success", false);
                return ResponseEntity.notFound().build();
            }

        } catch (Exception e) {
            System.err.println("❌ Error getting user by ID: " + e.getMessage());
            e.printStackTrace();
            response.put("message", "Failed to retrieve user: " + e.getMessage());
            response.put("success", false);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping(value = "/email/{email}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> getUserByEmail(@PathVariable String email) {
        Map<String, Object> response = new HashMap<>();

        try {
            System.out.println("📧 Getting user by email: " + email);
            Optional<User> userOptional = userRepository.findByEmail(email);

            if (userOptional.isPresent()) {
                User user = userOptional.get();
                response.put("message", "User found successfully");
                response.put("success", true);
                response.put("user", convertUserToSafeMap(user));
                return ResponseEntity.ok(response);
            } else {
                response.put("message", "User not found with email: " + email);
                response.put("success", false);
                return ResponseEntity.notFound().build();
            }

        } catch (Exception e) {
            System.err.println("❌ Error getting user by email: " + e.getMessage());
            e.printStackTrace();
            response.put("message", "Failed to retrieve user: " + e.getMessage());
            response.put("success", false);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> searchUsers(@RequestParam String name) {
        Map<String, Object> response = new HashMap<>();

        try {
            System.out.println("🔍 Searching users by name: " + name);
            List<User> users = userRepository.findByNameContaining(name);

            List<Map<String, Object>> userList = users.stream()
                    .map(this::convertUserToSafeMap)
                    .toList();

            response.put("message", "Search completed successfully");
            response.put("success", true);
            response.put("users", userList);
            response.put("count", userList.size());
            response.put("searchTerm", name);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            System.err.println("❌ Error searching users: " + e.getMessage());
            e.printStackTrace();
            response.put("message", "Failed to search users: " + e.getMessage());
            response.put("success", false);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping(value = "/exists/{email}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> checkEmailExists(@PathVariable String email) {
        Map<String, Object> response = new HashMap<>();

        try {
            System.out.println("🔍 Checking if email exists: " + email);
            boolean exists = userRepository.existsByEmail(email);

            response.put("message", exists ? "Email exists" : "Email available");
            response.put("success", true);
            response.put("exists", exists);
            response.put("email", email);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            System.err.println("❌ Error checking email existence: " + e.getMessage());
            e.printStackTrace();
            response.put("message", "Failed to check email: " + e.getMessage());
            response.put("success", false);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    private Map<String, Object> convertUserToSafeMap(User user) {
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("userId", user.getUserId());
        userMap.put("firstName", user.getFirstName());
        userMap.put("lastName", user.getLastName());
        userMap.put("email", user.getEmail());
        userMap.put("phoneNumber", user.getPhoneNumber());
        userMap.put("createdAt", user.getCreatedAt());
        userMap.put("fullName", (user.getFirstName() != null ? user.getFirstName() : "") + " " +
                (user.getLastName() != null ? user.getLastName() : ""));
        return userMap;
    }
}
