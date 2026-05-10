package com.manjo.paymentgateway.repository;

import com.manjo.paymentgateway.entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {
    Optional<Session> findByRefreshToken(String refreshToken);
    
    // Untuk fitur logout: cabut semua session user
    void deleteByUser_Id(Long userId);
}
