package com.manjo.paymentgateway.config;

import com.manjo.paymentgateway.repository.UserRepository;
import com.manjo.paymentgateway.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final AuthService authService;
    private final org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {
            // Kita tambahkan parameter 'Administrator Manjo' dan 'admin@manjo.id'
            authService.register(
                "admin", 
                "admin123", 
                "Administrator Manjo", 
                "admin@manjo.id"
            );
            log.info("Default admin user created: admin/admin123");
        }
    }
}
