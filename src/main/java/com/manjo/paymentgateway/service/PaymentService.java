package com.manjo.paymentgateway.service;

import com.manjo.paymentgateway.constant.MessageConstants;
import com.manjo.paymentgateway.constant.TransactionStatus;
import com.manjo.paymentgateway.dto.request.GenerateQrRequest;
import com.manjo.paymentgateway.dto.request.PaymentCallbackRequest;
import com.manjo.paymentgateway.dto.response.GenerateQrResponse;
import com.manjo.paymentgateway.dto.response.PaymentResponse;
import com.manjo.paymentgateway.entity.Transaction;
import com.manjo.paymentgateway.exception.SignatureInvalidException;
import com.manjo.paymentgateway.exception.TransactionNotFoundException;
import com.manjo.paymentgateway.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {

    private final TransactionRepository transactionRepository;
    private final SignatureService signatureService;
    private final WebhookService webhookService;

    @org.springframework.beans.factory.annotation.Value("${app.payment.qr-expiry-minutes:15}")
    private int qrExpiryMinutes;

    @Transactional
    public GenerateQrResponse createTransaction(GenerateQrRequest request, String signature) {
        BigDecimal amount;
        try {
            amount = new BigDecimal(request.getAmount().getValue());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Format amount tidak valid: " + request.getAmount().getValue());
        }

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Nominal harus lebih besar dari 0");
        }

        boolean isValid = signatureService.validateSignature(
                request.getMerchantId(),
                request.getAmount().getValue(),
                request.getPartnerReferenceNo(),
                signature);

        if (!isValid) {
            throw new SignatureInvalidException("Tanda tangan (signature) tidak valid");
        }

        // Validasi Unik Partner Reference Number
        if (transactionRepository.existsByPartnerReferenceNumber(request.getPartnerReferenceNo())) {
            throw new IllegalStateException("Transaksi dengan nomor referensi tersebut sudah ada");
        }

        String referenceNo = "M" + String.format("%010d", Math.abs(System.nanoTime() % 10_000_000_000L));

        BigDecimal feeRate = new BigDecimal("0.007");
        BigDecimal fee = amount.multiply(feeRate).setScale(2, java.math.RoundingMode.HALF_UP);
        BigDecimal netAmount = amount.subtract(fee);

        Transaction trx = Transaction.builder()
                .merchantId(request.getMerchantId())
                .amount(amount)
                .fee(fee)
                .netAmount(netAmount)
                .currency(request.getAmount().getCurrency() != null ? request.getAmount().getCurrency() : "IDR")
                .partnerReferenceNumber(request.getPartnerReferenceNo())
                .referenceNumber(referenceNo)
                .paymentMethod(request.getPaymentMethod() != null ? request.getPaymentMethod() : "QR")
                .paymentChannel(
                        request.getPaymentMethod() != null && request.getPaymentMethod().equals("QR") ? "QRIS_MPM"
                                : "UNKNOWN")
                .callbackUrl(request.getCallbackUrl())
                .status(TransactionStatus.PENDING)
                .transactionDate(LocalDateTime.now())
                .expiryDate(LocalDateTime.now().plusMinutes(qrExpiryMinutes))
                .merchantName(request.getMerchantName() != null ? request.getMerchantName() : "MANJO MERCHANT")
                .description(request.getDescription() != null ? request.getDescription() : "Payment for " + referenceNo)
                .mpan(request.getMpan() != null ? request.getMpan() : "MPAN-" + (int) (Math.random() * 1000000))
                .build();

        transactionRepository.save(trx);
        webhookService.sendNotification(trx);

        String qrPlaceholder = "00020101021226620015ID.CO.MANJO.WWW01189360085801751859910210EP278421820303UMI51530014ID.CO.QRIS.WWW0215ID102106515"
                + referenceNo;

        return GenerateQrResponse.builder()
                .responseCode(MessageConstants.CODE_GENERATE_SUCCESS)
                .responseMessage(MessageConstants.SUCCESS)
                .referenceNo(referenceNo)
                .partnerReferenceNo(request.getPartnerReferenceNo())
                .qrContent(trx.getPaymentMethod().equals("QR") ? qrPlaceholder : null)
                .build();
    }

    @Transactional
    public PaymentResponse processCallback(PaymentCallbackRequest request, String signature) {
        boolean isValid = signatureService.validateCallbackSignature(
                request.getOriginalReferenceNo(),
                request.getAmount().getValue(),
                request.getTransactionStatusDesc(),
                signature);

        if (!isValid)
            throw new SignatureInvalidException("Tanda tangan (signature) tidak valid");

        Transaction trx = transactionRepository.findByReferenceNumber(request.getOriginalReferenceNo())
                .orElseThrow(() -> new TransactionNotFoundException("Transaksi tidak ditemukan"));

        // Validasi: Status saat ini harus PENDING
        if (!TransactionStatus.PENDING.equalsIgnoreCase(trx.getStatus())) {
            throw new IllegalStateException("Transaksi sudah diproses sebelumnya dengan status: " + trx.getStatus());
        }

        LocalDateTime paidTime;
        try {
            paidTime = OffsetDateTime.parse(request.getPaidTime()).toLocalDateTime();
        } catch (Exception e) {
            paidTime = LocalDateTime.now();
        }

        // VALIDASI: Waktu pembayaran tidak boleh melebihi expiry_date
        if (paidTime.isAfter(trx.getExpiryDate())) {
            log.warn("Payment rejected: Paid time {} is after expiry date {} for ref: {}", 
                    paidTime, trx.getExpiryDate(), trx.getReferenceNumber());
            
            trx.setStatus(TransactionStatus.EXPIRED);
            transactionRepository.save(trx);
            webhookService.sendNotification(trx);
            
            return PaymentResponse.builder()
                    .responseCode(MessageConstants.CODE_BAD_REQUEST)
                    .responseMessage("Transaksi Gagal: Waktu pembayaran telah kedaluwarsa")
                    .build();
        }

        String normalizedStatus = request.getTransactionStatusDesc().toUpperCase();
        if ("CANCELED".equals(normalizedStatus)) {
            normalizedStatus = TransactionStatus.CANCELLED;
        }
        
        trx.setStatus(normalizedStatus);
        if (TransactionStatus.SUCCESS.equalsIgnoreCase(trx.getStatus())) {
            trx.setPaidDate(paidTime);
        } else {
            trx.setPaidDate(null);
        }

        transactionRepository.save(trx);
        webhookService.sendNotification(trx);

        return PaymentResponse.builder()
                .responseCode(MessageConstants.CODE_NOTIFY_SUCCESS)
                .responseMessage(MessageConstants.SUCCESS)
                .build();
    }

    @Transactional(readOnly = true)
    public PaymentResponse queryTransaction(String trxId, String referenceNumber) {
        Optional<Transaction> trxOpt = Optional.empty();
        if (referenceNumber != null && !referenceNumber.isEmpty()) {
            trxOpt = transactionRepository.findByReferenceNumber(referenceNumber);
        } else if (trxId != null && !trxId.isEmpty()) {
            // trxId dipetakan ke partnerReferenceNumber sesuai diskusi
            trxOpt = transactionRepository.findByPartnerReferenceNumber(trxId);
        }

        Transaction trx = trxOpt.orElseThrow(() -> new TransactionNotFoundException("Transaksi tidak ditemukan"));

        return PaymentResponse.builder()
                .responseCode(MessageConstants.CODE_QUERY_SUCCESS)
                .responseMessage(MessageConstants.SUCCESS)
                .referenceNo(trx.getReferenceNumber())
                .partnerReferenceNo(trx.getPartnerReferenceNumber())
                .amount(new com.manjo.paymentgateway.dto.AmountDto(trx.getAmount().toString(), trx.getCurrency()))
                .transactionStatusDesc(trx.getStatus())
                .build();
    }

    @Transactional
    public PaymentResponse cancelTransaction(String referenceNumber) {
        Transaction trx = transactionRepository.findByReferenceNumber(referenceNumber)
                .orElseThrow(() -> new TransactionNotFoundException("Transaksi tidak ditemukan"));

        if (!TransactionStatus.PENDING.equalsIgnoreCase(trx.getStatus())) {
            throw new IllegalStateException("Hanya transaksi PENDING yang dapat dibatalkan");
        }

        trx.setStatus(TransactionStatus.CANCELLED);
        transactionRepository.save(trx);
        webhookService.sendNotification(trx);

        return PaymentResponse.builder()
                .responseCode(MessageConstants.CODE_SUCCESS)
                .responseMessage("Transaksi berhasil dibatalkan")
                .transactionStatusDesc(TransactionStatus.CANCELLED)
                .build();
    }
}
