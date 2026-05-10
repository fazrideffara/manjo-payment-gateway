package com.manjo.paymentgateway.service;

import com.manjo.paymentgateway.entity.Transaction;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class WebhookService {

    private final RestTemplate restTemplate;

    public void sendNotification(Transaction transaction) {
        if (transaction.getCallbackUrl() == null || transaction.getCallbackUrl().isEmpty()) {
            log.info("No callback URL for transaction: {}, skipping notification", transaction.getReferenceNumber());
            return;
        }

        Map<String, Object> payload = new HashMap<>();
        payload.put("referenceNo", transaction.getReferenceNumber());
        payload.put("partnerReferenceNo", transaction.getTrxId());
        payload.put("status", transaction.getStatus());
        payload.put("amount", transaction.getAmount());
        payload.put("paymentMethod", transaction.getPaymentMethod());
        payload.put("paidDate", transaction.getPaidDate());

        log.info("Sending webhook notification to {}: status={}", transaction.getCallbackUrl(), transaction.getStatus());
        
        try {
            // Mengirim request HTTP nyata ke URL callback merchant
            restTemplate.postForEntity(transaction.getCallbackUrl(), payload, String.class);
            log.info("Webhook successfully sent to {}", transaction.getCallbackUrl());
        } catch (Exception e) {
            log.error("Failed to send webhook to {}: {}", transaction.getCallbackUrl(), e.getMessage());
        }
    }
}
