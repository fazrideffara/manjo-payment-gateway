package com.manjo.paymentgateway.service;

import com.manjo.paymentgateway.entity.Session;
import com.manjo.paymentgateway.entity.User;
import com.manjo.paymentgateway.repository.SessionRepository;
import com.manjo.paymentgateway.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final SessionRepository sessionRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public Map<String, String> register(String username, String password, String name, String email) {
        User user = User.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .name(name)
                .email(email)
                .role("ROLE_USER")
                .build();
        userRepository.save(Objects.requireNonNull(user));
        return login(username, password);
    }

    @Transactional
    public Map<String, String> login(String username, String password) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new org.springframework.security.authentication.BadCredentialsException("User tidak ditemukan"));

        // Generate Tokens
        String accessToken = jwtService.generateToken(username);
        String refreshToken = jwtService.generateRefreshToken(username);

        // Save Session
        Date expiryDate = jwtService.extractExpiration(refreshToken);
        Session session = Session.builder()
                .user(user)
                .refreshToken(refreshToken)
                .isRevoked(false)
                .lastLogin(LocalDateTime.now())
                .expiryDate(expiryDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime())
                .build();
        
        // Hapus session lama (opsional: agar hanya ada 1 session aktif)
        sessionRepository.deleteByUser_Id(user.getId());
        sessionRepository.save(Objects.requireNonNull(session));

        Map<String, String> tokens = new HashMap<>();
        tokens.put("accessToken", accessToken);
        tokens.put("refreshToken", refreshToken);
        tokens.put("name", user.getName());
        tokens.put("email", user.getEmail());
        tokens.put("role", user.getRole());
        return tokens;
    }

    @Transactional
    public Map<String, String> refreshToken(String refreshToken) {
        Session session = sessionRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new RuntimeException("Refresh Token tidak valid"));

        if (session.isRevoked() || session.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Refresh Token sudah kadaluwarsa atau dicabut");
        }

        String username = session.getUser().getUsername();
        String newAccessToken = jwtService.generateToken(username);

        Map<String, String> tokens = new HashMap<>();
        tokens.put("accessToken", newAccessToken);
        tokens.put("refreshToken", refreshToken);
        return tokens;
    }

    @Transactional
    public void logout(String refreshToken) {
        Session session = sessionRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new RuntimeException("Sesi tidak ditemukan"));
        session.setRevoked(true);
        sessionRepository.save(session);
    }

    @Transactional
    public void updateProfile(String username, String newName) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new org.springframework.security.authentication.BadCredentialsException("User tidak ditemukan"));
        
        if (newName != null && !newName.isEmpty()) {
            user.setName(newName);
        }
        
        userRepository.save(user);
    }
}
