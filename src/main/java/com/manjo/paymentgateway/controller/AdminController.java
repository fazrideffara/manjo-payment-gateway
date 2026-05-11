package com.manjo.paymentgateway.controller;

import com.manjo.paymentgateway.constant.ApiEndpoints;
import com.manjo.paymentgateway.constant.SwaggerConstants;
import com.manjo.paymentgateway.constant.TransactionStatus;
import com.manjo.paymentgateway.dto.response.DashboardStatsDto;
import com.manjo.paymentgateway.entity.Transaction;
import com.manjo.paymentgateway.repository.TransactionRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping(ApiEndpoints.ADMIN_BASE)
@CrossOrigin
@RequiredArgsConstructor
@Tag(name = SwaggerConstants.ADMIN_TAG, description = SwaggerConstants.ADMIN_DESC)
@SecurityRequirement(name = "JWT")
@Slf4j
public class AdminController {

    private final TransactionRepository transactionRepository;

    @GetMapping(ApiEndpoints.TRANSACTIONS)
    @Operation(summary = SwaggerConstants.GET_ALL_TRANSACTIONS_SUMMARY, description = SwaggerConstants.GET_ALL_TRANSACTIONS_DESC)
    public ResponseEntity<Page<Transaction>> getAllTransactions(
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("transactionDate").descending());
        
        Page<Transaction> transactions;
        if (status != null && !status.equalsIgnoreCase("ALL")) {
            transactions = transactionRepository.findAllByStatus(status, pageable);
        } else {
            transactions = transactionRepository.findAll(pageable);
        }
        
        log.info("Admin fetching transactions: page {}, size {}, found {} records", page, size, transactions.getTotalElements());
        return ResponseEntity.ok(transactions);
    }

    @GetMapping(ApiEndpoints.STATS)
    @Operation(summary = SwaggerConstants.GET_STATS_SUMMARY, description = SwaggerConstants.GET_STATS_DESC)
    public ResponseEntity<DashboardStatsDto> getStats() {
        List<Transaction> all = transactionRepository.findAll();
        
        long totalCount = all.size();
        long successCount = all.stream().filter(t -> TransactionStatus.SUCCESS.equalsIgnoreCase(t.getStatus())).count();
        long pendingCount = all.stream().filter(t -> TransactionStatus.PENDING.equalsIgnoreCase(t.getStatus())).count();
        
        BigDecimal revenue = all.stream()
                .filter(t -> TransactionStatus.SUCCESS.equalsIgnoreCase(t.getStatus()))
                .map(Transaction::getAmount)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        BigDecimal totalSuccessVolume = all.stream()
                .filter(t -> TransactionStatus.SUCCESS.equalsIgnoreCase(t.getStatus()))
                .map(Transaction::getNetAmount) // Menggunakan netAmount (sudah dipotong MDR)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        double successRate = totalCount == 0 ? 0 : (double) successCount / totalCount * 100;

        return ResponseEntity.ok(DashboardStatsDto.builder()
                .totalRevenue(String.format("Rp %,.0f", revenue))
                .activeTransactions(pendingCount)
                .successRate(String.format("%.1f%%", successRate))
                .settlementVolume(String.format("Rp %,.0f", totalSuccessVolume))
                .totalTransactions(totalCount)
                .build());
    }

}
