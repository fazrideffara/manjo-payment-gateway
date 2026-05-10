package com.manjo.paymentgateway.controller;

import com.manjo.paymentgateway.service.AuthService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(
                request.getUsername(), 
                request.getPassword(), 
                request.getName(), 
                request.getEmail()
        ));
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request.getUsername(), request.getPassword()));
    }

    @PostMapping("/refresh")
    public ResponseEntity<Map<String, String>> refresh(@RequestBody Map<String, String> request) {
        return ResponseEntity.ok(authService.refreshToken(request.get("refreshToken")));
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestBody Map<String, String> request) {
        authService.logout(request.get("refreshToken"));
        return ResponseEntity.ok("Logged out successfully");
    }

    @PutMapping("/profile")
    public ResponseEntity<String> updateProfile(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody UpdateProfileRequest request) {
        authService.updateProfile(userDetails.getUsername(), request.getName(), request.getUsername());
        return ResponseEntity.ok("Profile updated successfully");
    }

    @Data
    public static class LoginRequest {
        private String username;
        private String password;
    }

    @Data
    public static class RegisterRequest {
        private String username;
        private String password;
        private String name;
        private String email;
    }

    @Data
    public static class UpdateProfileRequest {
        private String name;
        private String username;
    }
}
