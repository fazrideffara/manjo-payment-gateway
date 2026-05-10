package com.manjo.paymentgateway.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "merchant_id", nullable = false, length = 50)
    private String merchantId;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    @Column(name = "trx_id", nullable = false, unique = true, length = 100)
    private String trxId;

    @Column(name = "partner_reference_number", nullable = false, length = 100)
    private String partnerReferenceNumber;

    @Column(name = "reference_number", nullable = false, unique = true, length = 100)
    private String referenceNumber;

    @Column(name = "payment_method", nullable = false, length = 20)
    private String paymentMethod; // QR, BANK_TRANSFER, CREDIT_CARD

    @Column(nullable = false, length = 20)
    @Builder.Default
    private String status = "PENDING";

    @Column(name = "callback_url")
    private String callbackUrl;

    @Column(name = "transaction_date", nullable = false)
    private LocalDateTime transactionDate;

    @Column(name = "paid_date")
    private LocalDateTime paidDate;

    @Column(name = "expiry_date")
    private LocalDateTime expiryDate;

    @Column(name = "mpan")
    private String mpan;

    @Column(name = "customer_user_no")
    private String customerUserNo;

    @Column(name = "merchant_name")
    private String merchantName;

    @Column(name = "payment_channel")
    private String paymentChannel; // MANDIRI, BCA, OVO, etc.

    @Column(name = "payment_code")
    private String paymentCode; // VA Number or Payment Ref

    @Column(name = "currency", length = 3)
    @Builder.Default
    private String currency = "IDR";

    @Column(precision = 19, scale = 2)
    private BigDecimal fee;

    @Column(name = "net_amount", precision = 19, scale = 2)
    private BigDecimal netAmount;

    @Column(name = "customer_name")
    private String customerName;

    @Column(name = "customer_email")
    private String customerEmail;

    @Column(name = "customer_phone")
    private String customerPhone;

    @Column(name = "terminal_id")
    private String terminalId;

    @Column(name = "description")
    private String description;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
