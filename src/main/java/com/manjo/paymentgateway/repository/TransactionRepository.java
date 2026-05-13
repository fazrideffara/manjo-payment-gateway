package com.manjo.paymentgateway.repository;

import com.manjo.paymentgateway.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    Optional<Transaction> findByReferenceNumber(String referenceNumber);
    Optional<Transaction> findByTrxId(String trxId);

    List<Transaction> findByMerchantIdOrderByTransactionDateDesc(String merchantId);

    Page<Transaction> findByMerchantIdAndStatus(
            String merchantId, String status, Pageable pageable
    );

    boolean existsByTrxId(String trxId);

    List<Transaction> findAllByStatusAndExpiryDateBefore(String status, java.time.LocalDateTime expiryDate);

    Page<Transaction> findAllByStatus(String status, Pageable pageable);

    Page<Transaction> findAllByStatusIn(List<String> statuses, Pageable pageable);
}
