package com.manjo.paymentgateway.service;

import com.manjo.paymentgateway.constant.MessageConstants;
import com.manjo.paymentgateway.constant.TransactionStatus;
import com.manjo.paymentgateway.dto.AmountDto;
import com.manjo.paymentgateway.dto.request.GenerateQrRequest;
import com.manjo.paymentgateway.dto.request.PaymentCallbackRequest;
import com.manjo.paymentgateway.dto.response.GenerateQrResponse;
import com.manjo.paymentgateway.dto.response.PaymentResponse;
import com.manjo.paymentgateway.entity.Transaction;
import com.manjo.paymentgateway.exception.SignatureInvalidException;
import com.manjo.paymentgateway.exception.TransactionNotFoundException;
import com.manjo.paymentgateway.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class PaymentServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private SignatureService signatureService;

    @Mock
    private WebhookService webhookService;

    @InjectMocks
    private PaymentService paymentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createTransaction_Success() {
        GenerateQrRequest request = new GenerateQrRequest();
        request.setMerchantId("M1");
        request.setPartnerReferenceNo("P1");
        request.setAmount(new AmountDto("100.00", "IDR"));
        request.setPaymentMethod("QR");
        
        when(signatureService.validateSignature(anyString(), anyString(), anyString(), anyString()))
                .thenReturn(true);
        when(transactionRepository.existsByTrxId(anyString())).thenReturn(false);

        GenerateQrResponse response = paymentService.createTransaction(request, "valid-sig");

        assertNotNull(response);
        assertEquals(MessageConstants.CODE_GENERATE_SUCCESS, response.getResponseCode());
        verify(transactionRepository, times(1)).save(any());
    }

    @Test
    void createTransaction_InvalidSignature_ThrowsException() {
        GenerateQrRequest request = new GenerateQrRequest();
        request.setAmount(new AmountDto("100.00", "IDR"));
        when(signatureService.validateSignature(any(), any(), any(), any()))
                .thenReturn(false);

        assertThrows(SignatureInvalidException.class, () -> 
            paymentService.createTransaction(request, "invalid-sig")
        );
    }

    @Test
    void createTransaction_InvalidAmountFormat_ThrowsException() {
        GenerateQrRequest request = new GenerateQrRequest();
        request.setAmount(new AmountDto("abc", "IDR"));
        when(signatureService.validateSignature(any(), any(), any(), any()))
                .thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> 
            paymentService.createTransaction(request, "valid-sig")
        );
    }

    @Test
    void queryTransaction_Success() {
        Transaction trx = Transaction.builder()
                .referenceNumber("REF1")
                .status(TransactionStatus.SUCCESS)
                .amount(new BigDecimal("100"))
                .build();
        
        when(transactionRepository.findByReferenceNumber("REF1")).thenReturn(Optional.of(trx));

        PaymentResponse response = paymentService.queryTransaction(null, "REF1");

        assertNotNull(response);
        assertEquals(MessageConstants.CODE_QUERY_SUCCESS, response.getResponseCode());
        assertEquals(TransactionStatus.SUCCESS, response.getTransactionStatusDesc());
    }

    @Test
    void queryTransaction_NotFound_ThrowsException() {
        when(transactionRepository.findByReferenceNumber(anyString())).thenReturn(Optional.empty());

        assertThrows(TransactionNotFoundException.class, () -> 
            paymentService.queryTransaction(null, "NON-EXISTENT")
        );
    }

    @Test
    void cancelTransaction_Success() {
        Transaction trx = Transaction.builder()
                .referenceNumber("REF1")
                .status(TransactionStatus.PENDING)
                .build();
        
        when(transactionRepository.findByReferenceNumber("REF1")).thenReturn(Optional.of(trx));

        PaymentResponse response = paymentService.cancelTransaction("REF1");

        assertNotNull(response);
        assertEquals(TransactionStatus.CANCELLED, trx.getStatus());
        verify(transactionRepository).save(trx);
    }

    @Test
    void processCallback_Success() {
        PaymentCallbackRequest request = new PaymentCallbackRequest();
        request.setOriginalReferenceNo("REF1");
        request.setTransactionStatusDesc("SUCCESS");
        request.setPaidTime("2025-01-01T10:00:00Z");
        request.setAmount(new AmountDto("100.00", "IDR"));

        Transaction trx = Transaction.builder()
                .referenceNumber("REF1")
                .status(TransactionStatus.PENDING)
                .build();

        when(signatureService.validateCallbackSignature(anyString(), anyString(), anyString(), anyString())).thenReturn(true);
        when(transactionRepository.findByReferenceNumber("REF1")).thenReturn(Optional.of(trx));

        PaymentResponse response = paymentService.processCallback(request, "valid-sig");

        assertNotNull(response);
        assertEquals(TransactionStatus.SUCCESS, trx.getStatus());
        verify(webhookService).sendNotification(any());
    }
}
