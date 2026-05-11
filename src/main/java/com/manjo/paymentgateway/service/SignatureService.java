package com.manjo.paymentgateway.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SignatureService {

    @Value("${app.signature.secret-key:manjo-secret-key-123}")
    private String secretKey;

    /**
     * Validasi Signature untuk Create QR
     * Rumus: HMAC-SHA256(secret, merchant_id + amount + partner_ref_no)
     */
    public boolean validateSignature(String merchantId, String amount,
                                   String partnerRefNo, String receivedSignature) {
        if (receivedSignature == null) return false;
        
        try {
            String payload = merchantId + amount + partnerRefNo;
            String computedSignature = computeHmacSha256(payload);
            
            if (!computedSignature.equals(receivedSignature)) {
                log.warn("HMAC Validation FAILED!");
                log.warn("Payload: [{}]", payload);
                log.warn("Computed: {}", computedSignature);
                log.warn("Received: {}", receivedSignature);
            }
            
            return MessageDigest.isEqual(
                    computedSignature.getBytes(StandardCharsets.UTF_8),
                    receivedSignature.getBytes(StandardCharsets.UTF_8)
            );
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Validasi Signature untuk Callback
     * Rumus: HMAC-SHA256(secret, originalRefNo + amount + status)
     */
    public boolean validateCallbackSignature(String originalRefNo, String amount,
                                           String status, String receivedSignature) {
        if (receivedSignature == null) return false;

        try {
            String payload = originalRefNo + amount + status;
            String computedSignature = computeHmacSha256(payload);
            return MessageDigest.isEqual(
                    computedSignature.getBytes(StandardCharsets.UTF_8),
                    receivedSignature.getBytes(StandardCharsets.UTF_8)
            );
        } catch (Exception e) {
            return false;
        }
    }

    private String computeHmacSha256(String data) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA256");
        SecretKeySpec keySpec = new SecretKeySpec(
                secretKey.getBytes(StandardCharsets.UTF_8), "HmacSHA256"
        );
        mac.init(keySpec);
        byte[] hash = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder();
        for (byte b : hash) sb.append(String.format("%02x", b));
        return sb.toString();
    }
}
