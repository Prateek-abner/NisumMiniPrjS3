package com.shop.ecommerce.service;

import com.shop.ecommerce.entity.User;
import com.shop.ecommerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public Map<String, Object> login(String email, String password) {
        Map<String, Object> response = new HashMap<>();

        try {
            System.out.println("🔐 UserService: Login attempt for email: " + email);

            if (email == null || email.trim().isEmpty()) {
                response.put("message", "Email is required");
                response.put("success", false);
                return response;
            }

            if (password == null || password.trim().isEmpty()) {
                response.put("message", "Password is required");
                response.put("success", false);
                return response;
            }

            Optional<User> userOptional = userRepository.findByEmail(email.trim().toLowerCase());

            if (userOptional.isPresent()) {
                User user = userOptional.get();
                if (user.getPassword().equals(password)) {
                    System.out.println("✅ UserService: Login successful for: " + user.getFirstName());
                    response.put("message", "Login successful");
                    response.put("success", true);
                    response.put("userId", user.getUserId());
                    response.put("firstName", user.getFirstName());
                    response.put("lastName", user.getLastName());
                    response.put("email", user.getEmail());
                    return response;
                } else {
                    System.out.println("❌ UserService: Invalid password for: " + email);
                    response.put("message", "Invalid email or password");
                    response.put("success", false);
                    return response;
                }
            } else {
                System.out.println("❌ UserService: User not found: " + email);
                response.put("message", "Invalid email or password");
                response.put("success", false);
                return response;
            }
        } catch (Exception e) {
            System.err.println("❌ UserService: Login error: " + e.getMessage());
            e.printStackTrace();
            response.put("message", "Login failed: " + e.getMessage());
            response.put("success", false);
            return response;
        }
    }

    public Map<String, Object> register(String firstName, String lastName, String email,
                                        String password, String phoneNumber) {
        Map<String, Object> response = new HashMap<>();

        try {
            System.out.println("📝 UserService: Registration attempt for: " + email);

            if (firstName == null || firstName.trim().isEmpty()) {
                response.put("message", "First name is required");
                response.put("success", false);
                return response;
            }

            if (lastName == null || lastName.trim().isEmpty()) {
                response.put("message", "Last name is required");
                response.put("success", false);
                return response;
            }

            if (email == null || email.trim().isEmpty()) {
                response.put("message", "Email is required");
                response.put("success", false);
                return response;
            }

            if (password == null || password.length() < 6) {
                response.put("message", "Password must be at least 6 characters");
                response.put("success", false);
                return response;
            }

            if (userRepository.existsByEmail(email.trim().toLowerCase())) {
                System.out.println("❌ UserService: Email already exists: " + email);
                response.put("message", "Email already exists. Please use a different email.");
                response.put("success", false);
                return response;
            }

            Integer nextUserId = getNextUserId();
            System.out.println("🆔 UserService: Next UserID: " + nextUserId);

            User newUser = new User();
            newUser.setUserId(nextUserId);
            newUser.setFirstName(firstName.trim());
            newUser.setLastName(lastName.trim());
            newUser.setPassword(password);
            newUser.setEmail(email.trim().toLowerCase());
            newUser.setPhoneNumber(phoneNumber);
            newUser.setCreatedAt(LocalDateTime.now());

            User savedUser = userRepository.save(newUser);
            System.out.println("✅ UserService: User registered successfully: " + savedUser.getUserId());

            response.put("message", "Registration successful! You can now login with your credentials.");
            response.put("success", true);
            response.put("userId", savedUser.getUserId());
            response.put("firstName", savedUser.getFirstName());
            response.put("lastName", savedUser.getLastName());
            response.put("email", savedUser.getEmail());
            return response;

        } catch (Exception e) {
            System.err.println("❌ UserService: Registration error: " + e.getMessage());
            e.printStackTrace();
            response.put("message", "Registration failed: " + e.getMessage());
            response.put("success", false);
            return response;
        }
    }

    public boolean existsByEmail(String email) {
        try {
            return userRepository.existsByEmail(email.trim().toLowerCase());
        } catch (Exception e) {
            System.err.println("❌ UserService: Error checking email: " + e.getMessage());
            return false;
        }
    }

    public Optional<User> getUserByEmail(String email) {
        try {
            return userRepository.findByEmail(email.trim().toLowerCase());
        } catch (Exception e) {
            System.err.println("❌ UserService: Error getting user by email: " + e.getMessage());
            return Optional.empty();
        }
    }

    public Optional<User> getUserById(Integer userId) {
        try {
            return userRepository.findById(userId);
        } catch (Exception e) {
            System.err.println("❌ UserService: Error getting user by ID: " + e.getMessage());
            return Optional.empty();
        }
    }

    private Integer getNextUserId() {
        try {
            Integer maxId = userRepository.findAll().stream()
                    .mapToInt(User::getUserId)
                    .max()
                    .orElse(4);
            return maxId + 1;
        } catch (Exception e) {
            System.out.println("⚠️ UserService: Error getting next UserID, using fallback: " + e.getMessage());
            return 5;
        }
    }
}
