package com.manjo.paymentgateway.controller;

import com.manjo.paymentgateway.constant.ApiEndpoints;
import com.manjo.paymentgateway.constant.SwaggerConstants;
import com.manjo.paymentgateway.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(ApiEndpoints.AUTH_BASE)
@CrossOrigin
@RequiredArgsConstructor
@Tag(name = SwaggerConstants.AUTH_TAG, description = SwaggerConstants.AUTH_DESC)
public class AuthController {

    private final AuthService authService;

    @PostMapping(ApiEndpoints.REGISTER)
    @Operation(summary = SwaggerConstants.REGISTER_SUMMARY)
    public ResponseEntity<Map<String, String>> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(
                request.getUsername(), 
                request.getPassword(), 
                request.getName(), 
                request.getEmail()
        ));
    }

    @PostMapping(ApiEndpoints.LOGIN)
    @Operation(summary = SwaggerConstants.LOGIN_SUMMARY)
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request.getUsername(), request.getPassword()));
    }

    @PostMapping(ApiEndpoints.REFRESH)
    @Operation(summary = SwaggerConstants.REFRESH_SUMMARY)
    public ResponseEntity<Map<String, String>> refresh(@RequestBody Map<String, String> request) {
        return ResponseEntity.ok(authService.refreshToken(request.get("refreshToken")));
    }

    @PostMapping(ApiEndpoints.LOGOUT)
    @Operation(summary = SwaggerConstants.LOGOUT_SUMMARY)
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<String> logout(@RequestBody Map<String, String> request) {
        authService.logout(request.get("refreshToken"));
        return ResponseEntity.ok("Logged out successfully");
    }

    @PutMapping(ApiEndpoints.PROFILE)
    @Operation(summary = SwaggerConstants.PROFILE_SUMMARY)
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<String> updateProfile(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody UpdateProfileRequest request) {
        authService.updateProfile(userDetails.getUsername(), request.getName());
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
    }
}
