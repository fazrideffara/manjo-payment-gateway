package com.manjo.paymentgateway.service;

import com.manjo.paymentgateway.constant.TransactionStatus;
import com.manjo.paymentgateway.entity.Transaction;
import com.manjo.paymentgateway.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionExpiryService {

    private final TransactionRepository transactionRepository;

    // Cek transaksi expired setiap 1 menit
    @Scheduled(fixedRate = 60000)
    @Transactional
    public void checkExpiredTransactions() {
        LocalDateTime now = LocalDateTime.now();
        List<Transaction> expiredTrx = transactionRepository.findAllByStatusAndExpiryDateBefore(
                TransactionStatus.PENDING, now);

        if (!expiredTrx.isEmpty()) {
            log.info("System found {} expired transactions. Updating status...", expiredTrx.size());
            expiredTrx.forEach(trx -> {
                trx.setStatus(TransactionStatus.EXPIRED);
                trx.setUpdatedAt(now);
            });
            transactionRepository.saveAll(expiredTrx);
            log.info("Batch update for expired transactions completed.");
        }
    }
}
