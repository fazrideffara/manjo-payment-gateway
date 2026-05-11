package com.manjo.paymentgateway.service;

import com.manjo.paymentgateway.entity.User;
import com.manjo.paymentgateway.repository.SessionRepository;
import com.manjo.paymentgateway.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private SessionRepository sessionRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtService jwtService;
    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void register_Success() {
        when(passwordEncoder.encode(anyString())).thenReturn("hashed-pwd");
        
        User user = User.builder()
                .username("testuser")
                .name("Test User")
                .email("test@manjo.id")
                .role("ROLE_USER")
                .build();
        
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(jwtService.generateToken(anyString())).thenReturn("access-token");
        when(jwtService.generateRefreshToken(anyString())).thenReturn("refresh-token");
        when(jwtService.extractExpiration(anyString())).thenReturn(new java.util.Date(System.currentTimeMillis() + 3600000));

        Map<String, String> result = authService.register("testuser", "pwd", "Test User", "test@manjo.id");

        assertNotNull(result);
        assertEquals("access-token", result.get("accessToken"));
        verify(userRepository, times(1)).save(any());
    }

    @Test
    void login_Success() {
        User user = User.builder()
                .username("admin")
                .name("Admin")
                .role("ROLE_ADMIN")
                .build();
        
        when(userRepository.findByUsername("admin")).thenReturn(Optional.of(user));
        when(jwtService.generateToken(anyString())).thenReturn("access-token");
        when(jwtService.generateRefreshToken(anyString())).thenReturn("refresh-token");
        when(jwtService.extractExpiration(anyString())).thenReturn(new java.util.Date(System.currentTimeMillis() + 3600000));

        Map<String, String> result = authService.login("admin", "admin123");

        assertNotNull(result);
        assertEquals("access-token", result.get("accessToken"));
        verify(sessionRepository, times(1)).save(any());
    }

    @Test
    void login_UserNotFound_ThrowsException() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> authService.login("ghost", "password"));
    }
}
